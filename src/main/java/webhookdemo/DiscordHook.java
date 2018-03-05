package webhookdemo;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class DiscordHook {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "https://discordapp.com/api/webhooks/419278265857867787/S2R44NRbj9vPW4PERg_HzrGs-sWQYBNYfnNy_G-lONbSJdvYcQmHctgzmqIW7aGcsE7q?wait=true";
		
		DiscordContent sendContent = new DiscordContent();
		sendContent.setContent("test content");
		
		String send = "{\"content\": \"test content from java\"}";
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(url);
        //form.param("value", "bar");
        
        //String response = target.request().get().toString();
        MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<String, Object>();
        myHeaders.add("Content-Type", "application/json");
        Response response = target.request(MediaType.TEXT_PLAIN)
        					.headers(myHeaders)
        					.post(Entity.json(send));
        System.out.println(response.getStatus());

	}

}
