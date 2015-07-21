package util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwaggerJsonUtil {

    public static void main(String[] args) {
        SwaggerJsonUtil swaggerJsonUtil = new SwaggerJsonUtil();
        swaggerJsonUtil.parseRouterFiles();

    }

    public String parseRouterFiles() {
        Map<String, List<RouterFields>> listOfMethodsByApiMap = new HashMap<String, List<RouterFields>>();

        InputStream is = getClass().getResourceAsStream("/routes");

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line = null;

        StringBuilder responseData = new StringBuilder();
        try {
            while ((line = in.readLine()) != null) {
                if (!line.matches("^#.*$") && line.split("\\s+").length >= 3) {
                    List<String> queryOrPathStringList = new ArrayList<>();
                    // System.out.println(line);
                    String[] ss = line.split("\\s+");
                    List<RouterFields> routerArray;
                    String apiPath = ss[1].trim();
                    String updatedApiPath = "";

                    if (apiPath.matches(".*[:].*$")) {
                        String[] pathSplit = apiPath.split("\\/");
                        int index = 0;
                        for (String indPath : pathSplit) {
                            String updatedPath = indPath;
                            if (indPath.matches(".*[:].*$")) {
                                queryOrPathStringList.add(indPath.substring(1));
                                // queryOrPathString = indPath.substring(1);
                                updatedPath = "{" + indPath.substring(1) + "}";
                            }
                            if (index > 0) {
                                updatedApiPath += "/" + updatedPath;
                            } else {
                                updatedApiPath += updatedPath;
                            }
                            index++;
                        }
                        // String[] pathParam = apiPath.split("\\:");
                        // apiPath = pathParam[0] + "{" + pathParam[1] + "}";
                        apiPath = updatedApiPath;

                    }
                    String apiType = ss[0].trim();
                    if (listOfMethodsByApiMap.get(apiPath) == null) {
                        routerArray = new ArrayList<>();
                    } else {
                        routerArray = listOfMethodsByApiMap.get(apiPath);
                    }
                    RouterFields routerFields = null;

                    switch (apiType) {
                        case "GET":
                            routerFields = new RouterFields();
                            routerFields.operation = apiType;
                            if (ss[2].matches(".*page[:]$")) {
                                routerFields.hasPageApi = true;
                            }
                            if (queryOrPathStringList.size() > 0) {
                                routerFields.fieldType = "string";
                                routerFields.fieldName = queryOrPathStringList;
                            }

                            break;
                        case "PUT":
                            routerFields = new RouterFields();
                            routerFields.operation = apiType;
                            if (ss[2].matches(".*page[:]$")) {
                                routerFields.hasPageApi = true;
                            }
                            if (queryOrPathStringList.size() > 0) {
                                routerFields.fieldType = "string";
                                routerFields.fieldName = queryOrPathStringList;
                            }
                            break;
                        case "POST":
                            routerFields = new RouterFields();
                            routerFields.operation = apiType;
                            if (queryOrPathStringList.size() > 0) {
                                routerFields.fieldType = "string";
                                routerFields.fieldName = queryOrPathStringList;
                            }
                            break;
                        default:
                            break;
                    }
                    if (routerFields != null) {
                        routerArray.add(routerFields);

                        listOfMethodsByApiMap.put(apiPath, routerArray);
                    }
                    /*if (routerFields != null) {
                        RouterFields routerFields1 = new RouterFields();
						routerFields1.setHasPageApi(false);
						routerFields1.setFieldName(routerFields.getFieldName());
						routerFields1.setFieldType(routerFields.getFieldType());
						routerFields1.setOperation(routerFields.getOperation());
						routerArray.add(routerFields);

						listOfMethodsByApiMap.put(apiPath, routerArray);
					}*/

                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        is = getClass().getResourceAsStream("/swagger_template.json");
        in = new BufferedReader(new InputStreamReader(is));
        line = null;

        responseData = new StringBuilder();
        String templateJson = "";
        try {
            while ((line = in.readLine()) != null) {
                // System.out.println(line);
                responseData.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        templateJson = responseData.toString();
        String swaggerJson = "";
        boolean hasRoute = false;
        int totalNumberOfKeySet = listOfMethodsByApiMap.keySet().size();
        int keySetIndex = 1;
        for (String route : listOfMethodsByApiMap.keySet()) {
            swaggerJson += "\"" + route + "\": {";
            List<RouterFields> rFields = listOfMethodsByApiMap.get(route);
            int totalNumberOfRouter = rFields.size();
            int i = 1;
            boolean hasRouterField = false;
            for (RouterFields rf : rFields) {
                try {
                    swaggerJson += "\"" + rf.operation.toLowerCase() + "\":";
                    SwaggerPojo swaggerPojo = new SwaggerPojo();
                    String[] tags = new String[1];
                    tags[0] = route;
                    swaggerPojo.setTags(tags);
                    swaggerPojo.setDescription(route);
                    String[] produces = new String[1];
                    produces[0] = "application/json";
                    String[] consumes = new String[1];
                    consumes[0] = "application/json";
                    swaggerPojo.setProduces(produces);
                    swaggerPojo.setConsumes(consumes);

                    List<SwaggerParameters> swaggerParametersList = new ArrayList<SwaggerParameters>();

                    if (rf.operation.toLowerCase().equalsIgnoreCase("get")) {
                        if (rf.fieldName.size() > 0) {
                            for (String pathParam : rf.fieldName) {
                                SwaggerParameters swaggerParameters = new SwaggerParameters();
                                swaggerParameters.setIn("path");
                                swaggerParameters.setName(pathParam);
                                swaggerParameters.setRequired(true);
                                swaggerParameters.setType(rf.fieldType);
                                swaggerParametersList.add(swaggerParameters);
                            }
                        }
                        if (rf.hasPageApi) {
                            SwaggerParameters swaggerParameters = new SwaggerParameters();
                            swaggerParameters.setIn("query");
                            swaggerParameters.setName("page");
                            swaggerParameters.setRequired(false);
                            swaggerParameters.setType("string");
                            swaggerParametersList.add(swaggerParameters);
                        }
                    } else {
                        if (rf.fieldName.size() > 0) {
                            for (String pathParam : rf.fieldName) {
                                SwaggerParameters swaggerParameters1 = new SwaggerParameters();
                                swaggerParameters1.setIn("path");
                                swaggerParameters1.setName(pathParam);
                                swaggerParameters1.setRequired(true);
                                swaggerParameters1.setType(rf.fieldType);
                                swaggerParametersList.add(swaggerParameters1);

                            }
                        }
                        if (rf.hasPageApi) {
                            SwaggerParameters swaggerParameters = new SwaggerParameters();
                            swaggerParameters.setIn("query");
                            swaggerParameters.setName("page");
                            swaggerParameters.setRequired(false);
                            swaggerParameters.setType("string");
                            swaggerParametersList.add(swaggerParameters);
                        }
                        SwaggerParameters swaggerParameters2 = new SwaggerParameters();
                        swaggerParameters2.setIn("body");
                        swaggerParameters2.setName("body");
                        swaggerParameters2.setRequired(true);
                        swaggerParameters2.setType(null);
                        swaggerParametersList.add(swaggerParameters2);

                    }

                    if (swaggerParametersList.size() > 0) {
                        SwaggerParameters[] swaggerParametersArray = new SwaggerParameters[swaggerParametersList
                                .size()];
                        int swaggerParameterIndex = 0;
                        for (SwaggerParameters swaggerParameters : swaggerParametersList) {
                            swaggerParametersArray[swaggerParameterIndex] = swaggerParameters;
                            swaggerParameterIndex++;
                        }
                        swaggerPojo.setParameters(swaggerParametersArray);
                    } else {
                        swaggerPojo.setParameters(null);
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    String swaggerPojoAsString = "";
                    swaggerPojoAsString = objectMapper
                            .writeValueAsString(swaggerPojo);
                    // System.out.println(swaggerPojoAsString);
                    swaggerJson += swaggerPojoAsString;
                    if (i < totalNumberOfRouter) {
                        swaggerJson += ",";
                    }
                    hasRoute = true;
                    i++;

                } catch (Exception e) {
                    i++;
                }
            }
            if (keySetIndex < totalNumberOfKeySet && hasRoute) {
                swaggerJson += "},";
            } else {
                swaggerJson += "}";
            }
            keySetIndex++;
            hasRoute = false;

        }

        return templateJson.replace("ABCDEFG", swaggerJson);
    }

    class RouterFields {
        public String operation;
        public String fieldType;
        public List<String> fieldName = new ArrayList<>();
        public boolean hasPageApi;

        /**
         * @return the operation
         */
        public String getOperation() {
            return operation;
        }

        /**
         * @param operation the operation to set
         */
        public void setOperation(String operation) {
            this.operation = operation;
        }

        /**
         * @return the fieldType
         */
        public String getFieldType() {
            return fieldType;
        }

        /**
         * @param fieldType the fieldType to set
         */
        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        /**
         * @return the fieldName
         */
        public List<String> getFieldName() {
            return fieldName;
        }

        /**
         * @param fieldName the fieldName to set
         */
        public void setFieldName(List<String> fieldName) {
            this.fieldName = fieldName;
        }

        /**
         * @return the hasPageApi
         */
        public boolean isHasPageApi() {
            return hasPageApi;
        }

        /**
         * @param hasPageApi the hasPageApi to set
         */
        public void setHasPageApi(boolean hasPageApi) {
            this.hasPageApi = hasPageApi;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "RouterFields [operation=" + operation + ", fieldType="
                    + fieldType + ", fieldName=" + fieldName + ", hasPageApi="
                    + hasPageApi + "]";
        }

    }

}
