package 地理.kml解析;

public class dd {
  public static void main(String[] args) {
    String JSON =
        "{\n"
            + "    \"type\": \"Feature\", \n"
            + "    \"properties\": { }, \n"
            + "    \"geometry\": {\n"
            + "        \"type\": \"LineString\", \n"
            + "        \"coordinates\": [\n"
            + "            [\n"
            + "                119.30705698315252, \n"
            + "                26.022566359436638\n"
            + "            ], \n"
            + "            [\n"
            + "                119.30683284057635, \n"
            + "                26.01467155980447\n"
            + "            ], \n"
            + "            [\n"
            + "                119.30239979851157, \n"
            + "                26.008320853475727\n"
            + "            ]\n"
            + "        ]\n"
            + "    }\n"
            + "}\n";
    System.out.println(JSON);
  }
}
;
