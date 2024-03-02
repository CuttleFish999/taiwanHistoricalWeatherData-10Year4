document.addEventListener('DOMContentLoaded', function () {

    const stationSelect = document.querySelector("select");
    const query_btn = document.getElementById("query-btn");

    if(query_btn && stationSelect) {
        query_btn.addEventListener("click" , function(e){
            let stationSelectVal = stationSelect.value;
            fetchYourData(stationSelectVal).then(data => {
                const processedData = processChartData(data);
                renderChart(processedData);
            }).catch(error => console.error('Error:', error));
        });
    } else {
        console.error("找不到元素：stationSelect 或 query-btn");
    }
});

function fetchYourData(selectedValue) {
    console.log(selectedValue);

    return fetch(`/getSingleMonthData/${selectedValue}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        });
}



function processChartData(data) {
    // 處理數據
    const labels = data.map(item => `${item.Month}月`);
    const tempValues = data.map(item => item.TemperatureMean);
    return {
        labels: labels,
        tempValues: tempValues
    };
}

function renderChart(processedData) {
    let myChart = echarts.init(document.getElementById('resultSection'));

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
            data: processedData.tempValues,
            type: 'line',
            smooth: true,
            areaStyle: {}
        }]
    };

    myChart.setOption(option);
}
