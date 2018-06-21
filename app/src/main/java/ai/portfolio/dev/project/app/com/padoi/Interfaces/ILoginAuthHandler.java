package ai.portfolio.dev.project.app.com.padoi.Interfaces;

/**
 * This interface will handle any Authentication error via HTTP requests
 */
public interface ILoginAuthHandler {
  RuntimeException networkError(String errMessage);
}
