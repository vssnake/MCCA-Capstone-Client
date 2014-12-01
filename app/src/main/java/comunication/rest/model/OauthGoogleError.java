package comunication.rest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unai on 09/10/2014.
 */
public class OauthGoogleError {
    public OauthGoogleError(){}

    private Error error;

    public Error getError() {
        return error;
    }


    public class Error{
        private List<Errors> errors;

        private String code;
        private String message;

        public Error(){}


        public List<Errors> getErrors() {
            return errors;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public class Errors{
            private String domain;
            private String reason;
            private String message;
            private String locationType;
            private String location;

            public Errors(){}

            public String getDomain() {
                return domain;
            }

            public String getReason() {
                return reason;
            }

            public String getMessage() {
                return message;
            }

            public String getLocationType() {
                return locationType;
            }

            public String getLocation() {
                return location;
            }
        }

    }

}
