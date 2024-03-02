function processAirPressureData(data) {
    const labels = data.map(item => `${item.Month}月`);
    const airPressureValues = data.map(item => item.AirPressure);
    return {
        labels: labels,
        airPressureValues: airPressureValues
    };
}

function renderAirPressureChart(processedData) {
    let myChart = echarts.init(document.getElementById('airPressureResultSection'));

    let option = {
        title: {
            text: '氣壓 (hPa)',
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
            data: processedData.airPressureValues,
            type: 'line',
            smooth: true,
            areaStyle: {
                color: 'purple'
            },
            itemStyle: {
                color: 'purple'
            },
            lineStyle: {
                color: 'purple'
            }
        }]
    };

    myChart.setOption(option);
}
