function processTemperatureData(data) {
    // Process temperature data
    const labels = data.map(item => `${item.Month}月`);
    const temperatureValues = data.map(item => item.TemperatureMean);
    return {
        labels: labels,
        temperatureValues: temperatureValues
    };
}

function renderTemperatureChart(processedData) {
    let myChart = echarts.init(document.getElementById('airTemperatureResultSection'));

    let option = {
        title: {
            text: '溫度 (°C)',
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
            data: processedData.temperatureValues,
            type: 'line',
            smooth: true,
            areaStyle: {
                color: 'red'
            },
            itemStyle: {
                color: 'red'
            },
            lineStyle: {
                color: 'red'
            }
        }]
    };

    myChart.setOption(option);
}

