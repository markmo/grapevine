import model.Reply;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.StringToTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by markmo on 3/04/2016.
 */
public class App {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://forums.whirlpool.net.au/forum-replies.cfm?t=2429582&p=66").get();
        List<Reply> replies = new ArrayList<>();
        Elements replyElems = doc.select(".reply");
        for (Element replyElem : replyElems) {
            Element userElem = replyElem.select(".username a").first();
            String userHref = userElem.attr("href");
            String userId = userHref.substring(userHref.lastIndexOf('/') + 1);
            String userName = userElem.select(".bu_name").text();
            String userGroup = replyElem.select(".usergroup").text();
            Element replyTextElem = replyElem.select(".replytext").first();
            Elements paraElems = replyTextElem.select("p");
            StringJoiner paras = new StringJoiner("\n");
            for (Element paraElem : paraElems) {
                paras.add(paraElem.text());
            }
            String replyText = paras.toString();
            String dateStr = replyElem.select(".date").text();
            Date date = StringToTime.apply(dateStr);
            Reply reply = new Reply(userId, userName, userGroup, replyText, date);
            replies.add(reply);
            System.out.println(reply);
        }
    }
}
