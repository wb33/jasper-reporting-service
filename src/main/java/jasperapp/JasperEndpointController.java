package jasperapp;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.Map;


@RestController
@RequestMapping(value = "/reports")
public class JasperEndpointController {
    private static final String FILE_FORMAT = "format";

    private static final String DATASOURCE = "datasource";

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    /* Example http://localhost:8080/reports/rpt_example/?format=pdf&id=0 */

    @RequestMapping(value = "/{reportname}/**", method = RequestMethod.GET)
    public ModelAndView getRptByParam(final ModelMap modelMap, ModelAndView modelAndView, @PathVariable("reportname") final String reportname,  HttpServletRequest request) {

        Map<String, String[]> map = request.getParameterMap();

        // connecting to H2
        modelMap.put(DATASOURCE, dataSource);
        String format = modelMap.entrySet().stream()
                .filter(e -> e.getValue().equals("format"))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("pdf");
//
        map.forEach( (String k, String[] v) -> modelMap.put(k, v[0]));

        //It is important that the underlying Jasper Report supports the Query parameters
        modelMap.put(FILE_FORMAT, format);

        modelAndView = new ModelAndView(reportname, modelMap);
        return modelAndView;
    }
}

