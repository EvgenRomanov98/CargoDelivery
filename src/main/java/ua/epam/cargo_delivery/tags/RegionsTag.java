package ua.epam.cargo_delivery.tags;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.model.db.City;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegionsTag extends TagSupport {
    private final Logger log = LogManager.getLogger(RegionsTag.class);

    @Override
    public int doStartTag() throws JspException {
        try {
            City region = (City) pageContext.getAttribute("region");
            String lang = pageContext.getAttribute("lang").toString();
            Locale locale = new Locale(lang);
            ResourceBundle b = ResourceBundle.getBundle("messages", locale);
            pageContext.getOut().print(b.getString(region.getLocaleKey()));
        } catch (IOException e) {
            log.error("", e);
        }
        return SKIP_BODY;
    }
}
