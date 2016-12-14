package cu.uci.plagicoj.scpdt;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import cu.uci.plagicoj.config.DIConfiguration;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import cu.uci.plagicoj.entities.SourceCode;
import cu.uci.plagicoj.db.PlagicojResult;
import cu.uci.plagicoj.db.Source;
import cu.uci.plagicoj.db.Source_;
import cu.uci.plagicoj.detectors.DetectorResult;
import cu.uci.plagicoj.detectors.impl.StringTilingDetector;
import cu.uci.plagicoj.utils.Language;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SCPDT implements Runnable {

    public static final String LOW_PRIORITY_QUEUE_NAME = "pcojOrdersLowPriority";
    public static final String MEDIUM_PRIORITY_QUEUE_NAME = "pcojOrdersMediumPriority";
    public static final String HIGH_PRIORITY_QUEUE_NAME = "pcojOrdersHighPriority";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Jedis jedis;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Consumer lowPriorityConsumer;

    @Autowired
    private Consumer meddiumPriorityConsumer;

    @Autowired
    private Consumer highPriorityConsumer;

    @Autowired
    private Semaphore lock;

    private Connection connection;
    
    private Channel currentChannel;
    
    private ConfigurableApplicationContext context;

    private long timeout;
    
    public SCPDT() {
        this.timeout = Long.MAX_VALUE;
    }

    private void Connect() throws IOException {
        connection = connectionFactory.newConnection();
        lowPriorityConsumer.setConnection(connection);
        meddiumPriorityConsumer.setConnection(connection);
        highPriorityConsumer.setConnection(connection);

        new Thread(lowPriorityConsumer).start();
        new Thread(meddiumPriorityConsumer).start();
        new Thread(highPriorityConsumer).start();
    }

    private QueueingConsumer.Delivery nextDelivery() throws IOException, InterruptedException {
        QueueingConsumer.Delivery delivery;

        if (!lock.tryAcquire(timeout, TimeUnit.MILLISECONDS)) return null;
        
        if ((delivery = highPriorityConsumer.popDelivery()) != null) {
            currentChannel = highPriorityConsumer.getChannel();

        } else if ((delivery = meddiumPriorityConsumer.popDelivery()) != null) {
            currentChannel = meddiumPriorityConsumer.getChannel();

        } else if ((delivery = lowPriorityConsumer.popDelivery()) != null) {
            currentChannel = lowPriorityConsumer.getChannel();
        }

        
        return delivery;
    }

    void terminate() throws IOException{
        lowPriorityConsumer.getChannel().close();
        meddiumPriorityConsumer.getChannel().close();
        highPriorityConsumer.getChannel().close();
        connection.close();
    }
    
    /*
     * {"ssid": 493174,"dsid":493172,"sslang": "Cpp","dslang": "Cpp"}
     */
    public void Detect() throws IOException, InterruptedException, JSONException {
        Connect();        
        while (true) {
            QueueingConsumer.Delivery delivery = nextDelivery();
            
            if (delivery == null) {
                terminate();
                return;
            }

            String message = new String(delivery.getBody());
            
            Logger.getLogger(SCPDT.class.getName()).log(Level.INFO, "Received: {0}", message);
            
            JSONObject jsonObject = new JSONObject(message); 
            
            String a = jsonObject.toString();
            int sourceSID = jsonObject.getInt("ssid");
            int destinationSID = jsonObject.getInt("dsid");

            Source sourceSubmission, destinationSubmission;
            
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Source> cq;
            TypedQuery<Source> q;
            Root<Source> source;

            if (jedis.exists(Integer.toString(sourceSID))) {
                sourceSubmission = Source.create();
                sourceSubmission.setCode(jedis.get(Integer.toString(sourceSID)));
            } else {
                cq = cb.createQuery(Source.class);
                source = cq.from(Source.class);
                cq.select(source);
                cq.where(source.get(Source_.sid).in(sourceSID));
                q = entityManager.createQuery(cq);
                sourceSubmission = q.getSingleResult();
                jedis.set(Integer.toString(sourceSID), sourceSubmission.getCode());
            }

            if (jedis.exists(Integer.toString(destinationSID))) {
                destinationSubmission = Source.create();
                destinationSubmission.setCode(jedis.get(Integer.toString(destinationSID)));
            } else {
                cq = cb.createQuery(Source.class);
                source = cq.from(Source.class);
                cq.select(source);
                cq.where(source.get(Source_.sid).in(destinationSID));

                q = entityManager.createQuery(cq);
                destinationSubmission = q.getSingleResult();

                jedis.set(Integer.toString(destinationSID), destinationSubmission.getCode());
            }

            JSONObject answer = new JSONObject();
            answer.put("ssid", sourceSID);
            answer.put("dsid", destinationSID);

            try {
                Language sourceSubmissionLanguage = Language.valueOf(jsonObject.getString("sslang"));
                Language destinationSubmissionLanguage = Language.valueOf(jsonObject.getString("dslang"));
            } catch (IllegalArgumentException jSONException) {
                answer.put("dictum", -1);
                answer.put("status", "UNSUPPORTED_LANGUAGE");

                currentChannel.basicPublish("", delivery.getProperties().getReplyTo(), null, answer.toString().getBytes("UTF-8"));
                currentChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                continue;
            }

            SourceCode sourceSC = new SourceCode(sourceSubmission.getCode(), Language.valueOf(jsonObject.getString("sslang")));
            SourceCode destinationSC = new SourceCode(destinationSubmission.getCode(), Language.valueOf(jsonObject.getString("dslang")));

            StringTilingDetector stringTilingDetector = new StringTilingDetector(sourceSC, destinationSC);

            DetectorResult detectPlagiarism = stringTilingDetector.detectPlagiarism();

            currentChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            double dictum = detectPlagiarism.getDictum();
            answer.put("dictum", dictum);

            PlagicojResult result = new PlagicojResult(sourceSID, destinationSID);
            result.setDictum(dictum);

            try {
                entityManager.getTransaction().begin();
                entityManager.persist(result);
                entityManager.getTransaction().commit();

            } catch (EntityExistsException | RollbackException ex) {
                Logger.getLogger(SCPDT.class.getName()).log(Level.FINEST, "Dictum already saved: {0}", answer.toString());    
            }

            currentChannel.basicPublish("", delivery.getProperties().getReplyTo(), null, answer.toString().getBytes("UTF-8"));
            
            Logger.getLogger(SCPDT.class.getName()).log(Level.INFO, "Answer: {0}", answer.toString());
        }
    }

    @Override
    public void run() {
        try {
            Detect();
        } catch (IOException | InterruptedException | JSONException ex) {
            Logger.getLogger(SCPDT.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            context.close();
        }
    }

    public static void init(){
        init(Long.MAX_VALUE);
    }
    
    public static void init(long timeout){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DIConfiguration.class);
        SCPDT scpdt = context.getBean(SCPDT.class);
        scpdt.setContext(context);
        scpdt.setTimeout(timeout);
        Thread thread = new Thread(scpdt);
        thread.start(); 
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, JSONException {
        //TODO: this must be adjusted    
        if (args.length > 0)
                Logger.getGlobal().setLevel(Level.parse(args[0]));
         init();
    }

    public void setContext(ConfigurableApplicationContext context) {
        this.context = context;        
    } 
   
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
