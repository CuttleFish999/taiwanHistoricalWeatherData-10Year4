function processPrecipitationData(data) {
    const labels = data.map(item => `${item.Month}月`);
    const precipitationValues = data.map(item => item.Precipitation);
    return {
        labels: labels,
        precipitationValues: precipitationValues
    };
}

function renderPrecipitationChart(processedData) {
    let myChart = echarts.init(document.getElementById('precipitationResultSection'));

    let option = {
        title: {
            text: '雨量 (mm)',
            left: 'center'
        },

        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: processedData.labels
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: processedData.precipitationValues,
            type: 'line',
            smooth: true,
            areaStyle: {
                color: 'blue'
            },
            itemStyle: {
                color: 'blue'
            },
            lineStyle: {
                color: 'blue'
            }
        }]
    };

    myChart.setOption(option);
}