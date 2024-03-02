document.addEventListener('DOMContentLoaded', function () {

    const stationSelect = document.querySelector("#city");
    const query_btn = document.getElementById("query-btn");

    if (query_btn && stationSelect) {
        query_btn.addEventListener("click", function (e) {
            e.preventDefault();

            let stationSelectVal = stationSelect.value;
            if (!areCheckboxesChecked()) {
                alert("請至少選擇一種氣象數據類型");
                return;
            }

            fetchYourData(stationSelectVal).then(data => {
                if (data) {
                    const processedData = processChartData(data);
                    renderChart(processedData);
                }
            }).catch(error => console.error('Error:', error));
        });
    } else {
        console.error("找不到元素：stationSelect 或 query-btn");
    }

});
function areCheckboxesChecked() {
    const checkboxes = document.querySelectorAll('.form-check-input');
    return Array.from(checkboxes).some(checkbox => checkbox.checked);
}
function fetchYourData(selectedValue) {
    const stationSelect = document.querySelector("#city");
    const temperatureCheckBox = document.getElementById("HumidityMean");

    if (selectedValue === "選擇縣市") {
        alert("請選擇一個縣市");
        stationSelect.style.color = "red";
        return Promise.reject(new Error("請選擇一個縣市"));
    } else {
        stationSelect.style.color = "black";
    }

    // Proceed only if temperatureCheckBox is checked
    if (temperatureCheckBox && temperatureCheckBox.checked) {
        return fetch(`/getSingleMonthData/${selectedValue}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('網路異常，請檢察網路');
                }
                return response.json();
            });
    } else {
        return Promise.resolve();
    }
}
function processChartData(data) {
    // Process data
    const labels = data.map(item => `${item.Month}月`);
    const tempValues = data.map(item => item.TemperatureMean);
    return {
        labels: labels,
        tempValues: tempValues
    };
}
function renderChart(processedData) {
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
            data: processedData.tempValues,
            type: 'line',
            smooth: true,
            areaStyle: {}
        }]
    };

    myChart.setOption(option);
}
