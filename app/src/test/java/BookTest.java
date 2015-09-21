import com.google.gson.Gson;
import junit.framework.TestCase;
import org.gbe.hugsward.model.Book;

/**
 * Created by gbe on 9/18/15.
 */
public class BookTest extends TestCase {

    Book b;

    private static final String JSON_ONE_BOOK = "{\n" +
            "    \"isbn\": \"c8fabf68-8374-48fe-a7ea-a00ccd07afff\",\n" +
            "    \"title\": \"Henri Potier à l'école des sorciers\",\n" +
            "    \"price\": 35,\n" +
            "    \"cover\": \"http://henri-potier.xebia.fr/hp0.jpg\"\n" +
            "  }\n";

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testDeserialize() throws Exception {
        Gson gson = new Gson();
        Book book = gson.fromJson(JSON_ONE_BOOK, Book.class);
        assert("Henri Potier à l'école des sorciers".equals(book.getTitle()));
    }
}