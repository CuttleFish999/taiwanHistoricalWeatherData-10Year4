function processHumidityData(data) {
    // Process humidity data
    const labels = data.map(item => `${item.Month}月`);
    const humidityValues = data.map(item => item.HumidityMean);
    return {
        labels: labels,
        humidityValues: humidityValues
    };
}

function renderHumidityChart(processedData) {
    let myChart = echarts.init(document.getElementById('relativeHumidityResultSection'));

    let option = {
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
            data: processedData.humidityValues,
            type: 'line',
            smooth: true,
            areaStyle: {}
        }]
    };

    myChart.setOption(option);
}
