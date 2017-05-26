<link href="resources/css/screen.css" rel="stylesheet" type="text/css"/>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Integer minimumFirefox = 40;
    Integer minimumSafari = 51; /* This number is 10 times the actual version number */
    Integer minimumChrome = 42;
    Integer minimumIE = 11;

    String ua = request.getHeader("User-Agent");

    boolean isMSIE = (ua != null && ua.indexOf("MSIE") != -1);
    boolean isChrome = (ua != null && ua.indexOf("Chrome/") != -1);
    boolean isSafari = (!isChrome && (ua != null && ua.indexOf("Safari/") != -1));
    boolean isFirefox = (ua != null && ua.indexOf("Firefox/") != -1);

    String version = "1";
    if (isFirefox) {
        // Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:13.0) Gecko/20100101 Firefox/13.0
        version = ua.replaceAll("^.*?Firefox/", "").replaceAll("\\.\\d+", "");
    } else if (isChrome) {
        // Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5
        version = ua.replaceAll("^.*?Chrome/(\\d+)\\..*$", "$1");
    } else if (isSafari) {
        // Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2
        version = ua.replaceAll("^.*?Version/(\\d+)\\.(\\d+).*$", "$1$2");
    } else if (isMSIE) {
        // Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)
        version = ua.replaceAll("^.*?MSIE\\s+(\\d+).*$", "$1");
    }
    boolean unsupportedBrowser = (isFirefox && Integer.parseInt(version) < minimumFirefox)
            || (isChrome && Integer.parseInt(version) < minimumChrome)
            || (isSafari && Integer.parseInt(version) < minimumSafari)
            || (isMSIE && Integer.parseInt(version) < minimumIE);
%>
<div style="text-align: center; margin-top: 10%">
    <% if (unsupportedBrowser) { %>
    <h4>Su navegador no es compatible con el Sistema!</h4>

    <div>Le recomendamos usar el navegador Mozilla Firefox, puedes descargala <a
            href="https://www.mozilla.org/en-US/firefox/all/#es-ES" target="_blank">aqu&iacute;</a>, o actualize su
        navegador!
    </div>
    <% } else { %>
    <c:redirect url="/login.html"/>
    <% } %>
</div>


