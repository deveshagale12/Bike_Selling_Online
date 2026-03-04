@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BikeSellingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeSellingApplication.class, args);
	}
	
	@Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("BikeThread-");
        executor.initialize();
        return executor;
    }

}
