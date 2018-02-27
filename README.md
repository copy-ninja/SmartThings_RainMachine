SmartThings RainMachine
=======================

**Update 2/27/2018:**
**The next Rainmachine update will re-enable HTTP access. Until then, if you'd like to manually enable it, you can adding the following lines to /etc/lighttpd.conf after the $SERVER[“socket”] == “0.0.0.0:8080” { } statement :
**

```else $SERVER["socket"] == "0.0.0.0:8081" {
   ssl.engine = "disable"
   $HTTP["url"] !~ "^/ui" {
        setenv.add-request-header = ("Host" => "Removed")
        proxy.server = ( "" =>
            (("host" => "127.0.0.1", "port" => 18080 ))
        )
     }
}
```


**Update 2/21/2018:**

**This integration is currently broken with Rainmachine firmware 4.0.925 and above (released 2/1/2018). This update disabled HTTP access to the device, and SmartThings currently does not support HTTPS with local integrations. I am evaluating possible options including petitioning Rainmachine to add an option to re-enable HTTP access. In the meantime, I suggest not updating for now if you want to keep your ST integration working.**

<a href="http://www.amazon.com/gp/product/B00FWYESVQ/ref=as_li_tl?ie=UTF8&camp=1789&creative=390957&creativeASIN=B00FWYESVQ&linkCode=as2&tag=githubcoderep-20&linkId=OJXHE5KG3FSYW5ZA">
<img border="0" src="http://ws-na.amazon-adsystem.com/widgets/q?_encoding=UTF8&ASIN=B00FWYESVQ&Format=_SL110_&ID=AsinImage&MarketPlace=US&ServiceVersion=20070822&WS=1&tag=githubcoderep-20" ></a>
<img src="http://ir-na.amazon-adsystem.com/e/ir?t=githubcoderep-20&l=as2&o=1&a=B00FWYESVQ" width="1" height="1" border="0" alt="" style="border:none !important; margin:0px !important;" /> 
<a href="http://www.amazon.com/gp/product/B00CT5PNBU/ref=as_li_tl?ie=UTF8&camp=1789&creative=390957&creativeASIN=B00CT5PNBU&linkCode=as2&tag=githubcoderep-20&linkId=TMHCNV23OPPKUPDV">
<img border="0" src="http://ws-na.amazon-adsystem.com/widgets/q?_encoding=UTF8&ASIN=B00CT5PNBU&Format=_SL110_&ID=AsinImage&MarketPlace=US&ServiceVersion=20070822&WS=1&tag=githubcoderep-20" ></a>
<img src="http://ir-na.amazon-adsystem.com/e/ir?t=githubcoderep-20&l=as2&o=1&a=B00CT5PNBU" width="1" height="1" border="0" alt="" style="border:none !important; margin:0px !important;" />
<a href="http://www.amazon.com/gp/product/B00CT5PNBU/ref=as_li_tl?ie=UTF8&camp=1789&creative=390957&creativeASIN=B00CT5PNBU&linkCode=as2&tag=githubcoderep-20&linkId=TMHCNV23OPPKUPDV">
<img border="0" src="http://i.imgur.com/c4QHSUKs.jpg" ></a>
<img src="http://i.imgur.com/c4QHSUKs.jpg" width="1" height="1" border="0" alt="" style="border:none !important; margin:0px !important;" />


For [prerequisites](https://github.com/brbeaird/SmartThings_RainMachine/wiki/Prerequisite) & [installation](https://github.com/brbeaird/SmartThings_RainMachine/wiki/Installation)  please refer to [wiki]( https://github.com/brbeaird/SmartThings_RainMachine/wiki)


### Donate:

If you love this app, feel free to donate.

[![PayPal - The safer, easier way to give online!](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif "Donate")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GJJA2ZYNWKS6Y)
 

