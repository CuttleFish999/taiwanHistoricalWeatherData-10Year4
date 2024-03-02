function processWindSpeedData(data) {
    const labels = data.map(item => `${item.Month}月`);
    const windSpeedValues = data.map(item => item.WindSpeed);
    return {
        labels: labels,
        windSpeedValues: windSpeedValues
    };
}

function renderWindSpeedChart(processedData) {
    let myChart = echarts.init(document.getElementById('windSpeedResultSection'));

    let option = {
        title: {
            text: '風速 (m/s)',
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
            data: processedData.windSpeedValues,
            type: 'line',
            smooth: true,
            areaStyle: {
                color: 'darkgreen'
            },
            itemStyle: {
                color: 'darkgreen'
            },
            lineStyle: {
                color: 'darkgreen'
            }
        }]
    };

    myChart.setOption(option);
}
