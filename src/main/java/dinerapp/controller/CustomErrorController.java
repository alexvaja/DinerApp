package dinerapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "error";
    }
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
 
//    @RequestMapping(value = "errors", method = RequestMethod.GET)
//    public String renderErrorPage(Model model, HttpServletRequest httpRequest) {
//         
//        String errorMsg = "";
//        int httpErrorCode = getErrorCode(httpRequest);
// 
//        switch (httpErrorCode) {
//            case 400: {
//                errorMsg = "Http Error Code: 400. Bad Request";
//                break;
//            }
//            case 401: {
//                errorMsg = "Http Error Code: 401. Unauthorized";
//                break;
//            }
//            case 404: {
//                errorMsg = "Http Error Code: 404. Resource not found";
//                break;
//            }
//            case 500: {
//                errorMsg = "Http Error Code: 500. Internal Server Error";
//                break;
//            }
//        }
//        
//        model.addAttribute("errorMsg", errorMsg);
//        return "views/errorPage";
//    }
//     
//    private int getErrorCode(HttpServletRequest httpRequest) {
//        return (Integer) httpRequest
//          .getAttribute("javax.servlet.error.status_code");
//    }
}