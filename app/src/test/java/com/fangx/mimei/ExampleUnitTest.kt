package com.fangx.mimei

import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testJsoup() {
        val htmlContent = "<p><img alt=\"\" src=\"http://7xi8d6.com1.z0.glb.clouddn.com/20180129074038_O3ydq4_Screenshot.jpeg\" /></p>\r\n\r\n<h3>iOS</h3>\r\n\r\n<ul>\r\n\t<li><a href=\"https://github.com/TalkingJourney/SCIndexView\" target=\"_blank\">SCIndexView provide a index view like Wechat.</a>&nbsp;(\u81ea\u7531\u6d41\u6c34)\r\n\r\n\t<ul>\r\n\t\t<li><a href=\"https://github.com/TalkingJourney/SCIndexView\" target=\"_blank\"><img src=\"http://img.gank.io/6d510bea-afd4-4489-ba42-d281783d7512?imageView2/2/w/460\" title=\"SCIndexView provide a index view like Wechat.\" /></a></li>\r\n\t</ul>\r\n\t</li>\r\n</ul>\r\n\r\n<h3>Android</h3>\r\n\r\n<ul>\r\n\t<li><a href=\"https://github.com/nekocode/MusicVisualization\" target=\"_blank\">Android \u4e0b\u7684\u97f3\u4e50\u53ef\u89c6\u5316</a>&nbsp;(nekocode)\r\n\r\n\t<ul>\r\n\t\t<li><a href=\"https://github.com/nekocode/MusicVisualization\" target=\"_blank\"><img src=\"http://img.gank.io/e0d29181-282e-4465-9965-1da81e0557d9?imageView2/2/w/460\" title=\"Android \u4e0b\u7684\u97f3\u4e50\u53ef\u89c6\u5316\" /></a></li>\r\n\t</ul>\r\n\t</li>\r\n\t<li><a href=\"https://fashare2015.github.io/2018/01/24/dynamic-load-learning-load-activity/\" target=\"_blank\">\u63d2\u4ef6\u5316\u7406\u89e3\u4e0e\u5b9e\u73b0 &mdash;&mdash; \u52a0\u8f7d Activity\u300c\u7c7b\u52a0\u8f7d\u7bc7\u300d</a>&nbsp;(\u6881\u5c71boy)\r\n\t<ul>\r\n\t\t<li><a href=\"https://fashare2015.github.io/2018/01/24/dynamic-load-learning-load-activity/\" target=\"_blank\"><img src=\"http://img.gank.io/a861c69f-02d2-46e8-b120-58ba4b3d97bf?imageView2/2/w/460\" title=\"\u63d2\u4ef6\u5316\u7406\u89e3\u4e0e\u5b9e\u73b0 \u2014\u2014 \u52a0\u8f7d Activity\u300c\u7c7b\u52a0\u8f7d\u7bc7\u300d\" /></a></li>\r\n\t</ul>\r\n\t</li>\r\n\t<li><a href=\"https://github.com/MasayukiSuda/Mp4Composer-android\" target=\"_blank\">This library generate an Mp4 movie using Android MediaCodec API and apply filter, scale, and rotate Mp4.</a>&nbsp;(None)\r\n\t<ul>\r\n\t\t<li><a href=\"https://github.com/MasayukiSuda/Mp4Composer-android\" target=\"_blank\"><img src=\"http://img.gank.io/6fe115da-20d7-4774-8f87-0b776ec7885c?imageView2/2/w/460\" title=\"This library generate an Mp4 movie using Android MediaCodec API and apply filter, scale, and rotate Mp4.\" /></a></li>\r\n\t</ul>\r\n\t</li>\r\n\t<li><a href=\"https://github.com/JoshuaRogue/BetterWay\" target=\"_blank\">\u4f7f\u7528MVP\u6a21\u5f0f\uff0c\u57fa\u4e8e\u9ad8\u5fb7\u5730\u56fe\u5f00\u53d1\uff0c\u5b9e\u73b0\u6bdb\u73bb\u7483\u7279\u6548</a>&nbsp;(None)\r\n\t<ul>\r\n\t</ul>\r\n\t</li>\r\n\t<li><a href=\"https://lizhaoxuan.github.io/2018/01/27/AccessibilityService%E5%88%86%E6%9E%90%E4%B8%8E%E9%98%B2%E5%BE%A1/\" target=\"_blank\">AccessibilityService\u7ecf\u5e38\u88ab\u9ed1\u4ea7\u7528\u6765\u5236\u4f5c\u5916\u6302\u5f71\u54cd\u6b63\u5e38\u7684\u7ade\u4e89\u73af\u5883\uff0c\u672c\u6587\u901a\u8fc7\u5bf9AccessibilityService\u6e90\u7801\u5206\u6790\u4ecb\u7ecd\u5176\u8fd0\u884c\u539f\u7406\u5e76\u7ed9\u51fa\u76f8\u5e94\u7684\u9632\u5fa1\u63aa\u65bd</a>&nbsp;(lizhaoxuan)\r\n\t<ul>\r\n\t</ul>\r\n\t</li>\r\n</ul>\r\n\r\n<h3>\u524d\u7aef</h3>\r\n\r\n<ul>\r\n\t<li><a href=\"https://zhuanlan.zhihu.com/p/32962454\" target=\"_blank\">\u524d\u7aef\u6bcf\u5468\u6e05\u5355\u7b2c 47 \u671f\uff1aNPM \u5e74\u5ea6\u62a5\u544a\u4e0e 2018 \u5c55\u671b\uff0cAirbnb React Router \u5b9e\u8df5</a>&nbsp;(\u738b\u4e0b\u9080\u6708\u718a(Chevalier))\r\n\r\n\t<ul>\r\n\t</ul>\r\n\t</li>\r\n</ul>\r\n\r\n<h3>\u62d3\u5c55\u8d44\u6e90</h3>\r\n\r\n<ul>\r\n\t<li><a href=\"https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&amp;mid=2247489312&amp;idx=1&amp;sn=e028fd71ad9b1c89fa12c9a90c0f7a4c\" target=\"_blank\">2018\u5e74\u6700\u53d7\u6b22\u8fce\u7684\u7f16\u7a0b\u6311\u6218\u7f51\u7ad9</a>&nbsp;(\u9648\u5b87\u660e)\r\n\r\n\t<ul>\r\n\t</ul>\r\n\t</li>\r\n\t<li><a href=\"https://love2.io/\" target=\"_blank\">Love2.io \u662f\u4e00\u4e2a\u5168\u65b0\u7684\u5f00\u6e90\u6280\u672f\u6587\u6863\u53d1\u5e03\u548c\u5206\u4eab\u5e73\u53f0\uff0c\u4e13\u6ce8\u4e8e\u63d0\u4f9b\u7528\u6237\u66f4\u4f18\u96c5\u7684\u6280\u672f\u6587\u6863\u5199\u4f5c\u548c\u9605\u8bfb\u4f53\u9a8c\u3002</a>&nbsp;(None)\r\n\t<ul>\r\n\t</ul>\r\n\t</li>\r\n</ul>\r\n\r\n<h3>\u4f11\u606f\u89c6\u9891</h3>\r\n\r\n<ul>\r\n\t<li><a href=\"http://www.bilibili.com/video/av4804332/\" target=\"_blank\">\u60ca\u4eba\u7684\u7bee\u7403\u5b9e\u9a8c\uff01\u7b80\u76f4\u4e0d\u6562\u76f8\u4fe1\u81ea\u5df1\u7684\u53cc\u773c\uff01</a>&nbsp;(LHF)\r\n\r\n\t<ul>\r\n\t</ul>\r\n\t</li>\r\n</ul>\r\n\r\n<p>\u611f\u8c22\u6240\u6709\u9ed8\u9ed8\u4ed8\u51fa\u7684\u7f16\u8f91\u4eec\uff0c\u613f\u5927\u5bb6\u6709\u7f8e\u597d\u4e00\u5929\u3002</p>\r\n"
        val document = Jsoup.parse(htmlContent)
        val select = document.selectFirst("h3")
        println(select.toString())
        println(select.text())
//        val src = select.attr("src")
//        println("src=$src")
//
//        val elements = document.getElementsByTag("img")
//        println(elements.toString())

    }
}
