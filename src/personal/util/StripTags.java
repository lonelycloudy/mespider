package personal.util;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;
import personal.util.StripTagsRegExp;

/**
* @author nathena.yang
http://hi.baidu.com/nathena/item/380c23c31f3c0b29ee466595
*performance such as php's strip_tags(str,<p>);
*/
public class StripTags
{
    private StripTagsRegExp reg = new StripTagsRegExp();

    public String parse(String html)
    {
        return this.parse( html,null );
    }

    public String parse(String html,String allows )
    {
        if( allows == null || allows.trim().length() == 0 )
        {
            html = reg.buildHTMLScriptRegex().replaceAll(html,"");
            html = reg.buildHTMLCssRegex().replaceAll(html,"");
            html = reg.buildHTMLTagRegex().replaceAll(html,"");   
            System.out.println("====== \n"+html);
            return html;
        }
        String tagre = "<([^>]*)>";   
        List<String> gs = reg.buildRegex(tagre).matcher(allows);
        String pr = "";       
        for(String g:gs)
        {
            pr = "(<"+g+"[^>]*>(?:\\s|\\S)*?</[^>]+>)";
            html = reg.buildRegex(pr).replaceAll(html,"<","####&lt####");           
        }       
        html = reg.buildHTMLTagRegex().replaceAll(html,"");
        html = html.replaceAll("####&lt####","<");
       
        return html;
    }

    public static void main(String[] args)
    {
        StripTags s = new StripTags();
        String result = s.parse("<div id=\"content\"><img src=\"http://a.com/a.gif\"/> <p>Test paragraph.</p><!-- Comment --> <a href=\"#fragment\">Other text</a><span class=\"footer\">footer</span></div>","<img>");
        System.out.println("====== \n"+result);
    }
}