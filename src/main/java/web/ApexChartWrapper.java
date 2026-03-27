package web;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;

import java.util.List;
import java.util.stream.Collectors;

public class ApexChartWrapper extends Div {

    public ApexChartWrapper(String chartType, String titlu, List<String> labels, List<Number> values, String[] colors) {

        String chartId = "chart_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        setId(chartId);


        setWidth("100%");
        setHeight("400px");


        UI.getCurrent().getPage().addJavaScript("https://cdn.jsdelivr.net/npm/apexcharts");


        String jsLabels = labels.stream().map(l -> "'" + l + "'").collect(Collectors.joining(","));
        String jsValues = values.stream().map(String::valueOf).collect(Collectors.joining(","));


        String jsColors = "";
        if (colors != null && colors.length > 0) {
            jsColors = "colors: [" + String.join(",", java.util.Arrays.stream(colors).map(c -> "'" + c + "'").toList()) + "],";
        }


        String jsCode = String.format(
                "setTimeout(function() {" +
                        "  var options = {" +
                        "    series: [%s]," +
                        "    chart: {" +
                        "      type: '%s'," +
                        "      height: 350" +
                        "    }," +
                        "    labels: [%s]," +
                        "    title: {" +
                        "      text: '%s'," +
                        "      align: 'left'" +
                        "    }," +
                        "    %s" +
                        "    plotOptions: {" +
                        "       bar: { borderRadius: 4, horizontal: true }" +
                        "    }," +
                        "    dataLabels: { enabled: true }" +
                        "  };" +
                        "  if ('%s' === 'bar') {" +
                        "     options.series = [{ data: [%s] }];" +
                        "     options.xaxis = { categories: [%s] };" +
                        "  }" +
                        "  var chart = new ApexCharts(document.getElementById('%s'), options);" +
                        "  chart.render();" +
                        "}, 500);",
                jsValues, chartType, jsLabels, titlu, jsColors, chartType, jsValues, jsLabels, chartId
        );


        UI.getCurrent().getPage().executeJs(jsCode);
    }
}