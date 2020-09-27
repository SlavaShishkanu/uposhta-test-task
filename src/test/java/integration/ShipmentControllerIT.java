package integration;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static java.lang.Integer.MIN_VALUE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opinta.dto.ShipmentDto;
import com.opinta.entity.Shipment;
import com.opinta.service.ShipmentService;
import integration.helper.TestHelper;

public class ShipmentControllerIT extends BaseControllerIT {
    private Shipment shipment;
    private int shipmentId = MIN_VALUE;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private TestHelper testHelper;

    @Before
    public void setUp() throws Exception {
        shipment = testHelper.createShipment();
        shipmentId = (int) shipment.getId();
    }

    @After
    public void tearDown() throws Exception {
        testHelper.deleteShipment(shipment);
    }

    @Test
    public void getShipments() throws Exception {
        when().get("/shipments").then().statusCode(SC_OK);
    }

    @Test
    public void getShipment() throws Exception {
        when().get("shipments/{id}", shipmentId).then().statusCode(SC_OK).body("id",
                equalTo(shipmentId));
    }

    @Test
    public void getShipment_notFound() throws Exception {
        when().get("/shipments/{id}", shipmentId + 1).then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createClient() throws Exception {
        // create
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/shipmentNew.json");
        jsonObject.put("senderId", (int) testHelper.createClient().getId());
        jsonObject.put("recipientId", (int) testHelper.createClient().getId());
        String expectedJson = jsonObject.toString();

        int newShipmentId = given().contentType("application/json;charset=UTF-8").body(expectedJson)
                .when().post("/shipments").then().extract().path("id");

        // check created data
        Shipment createdShipment = shipmentService.getEntityById(newShipmentId);
        ShipmentDto createdShipmentDto = shipmentService.getById(newShipmentId);
        createdShipmentDto.setParcels(null); // Parcels not checked
        ObjectMapper mapper = new ObjectMapper();

        // String actualJson = mapper.writeValueAsString(shipmentMapper.toDto(createdShipment));
        String actualJson = mapper.writeValueAsString(createdShipmentDto);

        jsonObject.remove("parcels");               // Parcels not checked
        jsonObject.put("price", 33);                // check calculations
        expectedJson = jsonObject.toString();

        JSONAssert.assertEquals(expectedJson, actualJson, false);

        // delete
        testHelper.deleteShipment(createdShipment);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateShipment() throws Exception {
        // update
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/shipmentNew.json");
        jsonObject.put("senderId", (int) testHelper.createClient().getId());
        jsonObject.put("recipientId", (int) testHelper.createClient().getId());
        String expectedJson = jsonObject.toString();

        given().contentType("application/json;charset=UTF-8").body(expectedJson).when()
                .put("/shipments/{id}", shipmentId).then().statusCode(SC_OK);

        ShipmentDto shipmentDto = shipmentService.getById(shipmentId); // changed
        shipmentDto.setParcels(null);                                  // Parcels not checked

        ObjectMapper mapper = new ObjectMapper();
        String actualJson = mapper.writeValueAsString(shipmentDto);

        jsonObject.put("price", 45);
        jsonObject.remove("parcels");                                   // Parcels not checked
        expectedJson = jsonObject.toString();

        JSONAssert.assertEquals(expectedJson, actualJson, false);

    }

    @Test
    public void deleteShipment() throws Exception {
        when().delete("/shipments/{id}", shipmentId).then().statusCode(SC_OK);
    }

    @Test
    public void deleteShipment_notFound() throws Exception {
        when().delete("/shipments/{id}", shipmentId + 1).then().statusCode(SC_NOT_FOUND);
    }
}
