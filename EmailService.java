@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async("taskExecutor")
    public void sendRegistrationMail(String toEmail, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to Bike Selling!");
        message.setText("Hello " + name + ",\n\nYour registration is complete. Welcome aboard!");
        mailSender.send(message);
    }
}