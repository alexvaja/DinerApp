package dinerapp.security.config;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpSessionConfig {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Bean
  public HttpSessionAttributeListener httpSessionAttributeListener() {
    return new HttpSessionAttributeListener() {
      @Override
      public void attributeAdded(final HttpSessionBindingEvent se) {

        LOGGER.info("Attribute Added following information");
        LOGGER.info("Attribute Name:" + se.getName());
        LOGGER.info("Attribute Value:" + se.getValue());
      }

      @Override
      public void attributeRemoved(final HttpSessionBindingEvent se) {
        LOGGER.info("attributeRemoved");
      }

      @Override
      public void attributeReplaced(final HttpSessionBindingEvent se) { // This method will be automatically called when
        LOGGER.info("Attribute Replaced following information");
        LOGGER.info("Attribute Name:" + se.getName());
        LOGGER.info("Attribute Old Value:" + se.getValue());
      }
    };
  }

  @Bean
  public HttpSessionListener httpSessionListener() {
    return new HttpSessionListener() {

      @Override
      public void sessionCreated(final HttpSessionEvent se) {
        LOGGER.info("Session Created with session id+" + se.getSession().getId());
      }

      @Override
      public void sessionDestroyed(final HttpSessionEvent se) {
        LOGGER.info("Session Destroyed, Session id:" + se.getSession().getId());
      }
    };
  }
}
