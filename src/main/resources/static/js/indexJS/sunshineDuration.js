function processSunshineDurationData(data) {
    const labels = data.map(item => `${item.Month}月`);
    const sunshineDurationValues = data.map(item => item.SunshineDuration);
    return {
        labels: labels,
        sunshineDurationValues: sunshineDurationValues
    };
}

function renderSunshineDurationChart(processedData) {
    let myChart = echarts.init(document.getElementById('sunshineDurationResultSection'));

    let option = {
        title: {
            text: '日照時數 (小時)',
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
            data: processedData.sunshineDurationValues,
            type: 'line',
            smooth: true,
            areaStyle: {
                color: 'orange'
            },
            itemStyle: {
                color: 'orange'
            },
            lineStyle: {
                color: 'orange'
            }
        }]
    };

    myChart.setOption(option);
}
