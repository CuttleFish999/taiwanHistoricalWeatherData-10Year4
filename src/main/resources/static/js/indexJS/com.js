function toggleSelection() {
    let checkboxes = document.querySelectorAll('.form-check-input');
    let cancelBtn = document.getElementById("cancelBtn");

    checkboxes.forEach(function(checkbox) {
        checkbox.checked = !checkbox.checked;
    });

    if (areCheckboxesChecked()) {
        cancelBtn.textContent = "取消選擇";
        cancelBtn.style.backgroundColor = "#007bff";
    } else {
        cancelBtn.textContent = "全選";
        cancelBtn.style.backgroundColor = "#28a745";
        // location.reload(true);
    }
}

function areCheckboxesChecked() {
    let checkboxes = document.querySelectorAll('.form-check-input');
    for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            return true;
        }
    }
    return false;
}

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
                    const temperatureCheckbox = document.getElementById("temperature");
                    const humidityCheckbox = document.getElementById("humidity");
                    const rainfallCheckbox = document.getElementById("rainfall");
                    const windSpeedCheckbox = document.getElementById("windSpeed");
                    const airPressureCheckbox = document.getElementById("airPressure");
                    const sunshineDurationCheckbox = document.getElementById("sunshine");
                    const temperatureChartContainer = document.getElementById('airTemperatureResultSection');
                    const humidityChartContainer = document.getElementById('relativeHumidityResultSection');
                    const rainfallResultSection = document.getElementById('precipitationResultSection');
                    const windSpeedResultSection = document.getElementById('windSpeedResultSection');
                    const airPressureResultSection = document.getElementById('airPressureResultSection');
                    const sunshineDurationResultSection = document.getElementById('sunshineDurationResultSection');

                    if (temperatureCheckbox && temperatureCheckbox.checked) {
                        const temperatureData = processTemperatureData(data);
                        renderTemperatureChart(temperatureData);
                    } else {
                        temperatureChartContainer.innerHTML = '';
                    }

                    if (humidityCheckbox && humidityCheckbox.checked) {
                        const humidityData = processHumidityData(data);
                        renderHumidityChart(humidityData);
                    } else {
                        humidityChartContainer.innerHTML = '';
                    }

                    if (rainfallCheckbox && rainfallCheckbox.checked) {
                        const precipitationData = processPrecipitationData(data);
                        renderPrecipitationChart(precipitationData);
                    } else {
                        rainfallResultSection.innerHTML = '';
                    }

                    if (windSpeedCheckbox && windSpeedCheckbox.checked) {
                        const windSpeedData = processWindSpeedData(data);
                        renderWindSpeedChart(windSpeedData);
                    } else {
                        windSpeedResultSection.innerHTML = '';
                    }

                    if (airPressureCheckbox && airPressureCheckbox.checked) {
                        const airPressureData = processAirPressureData(data);
                        renderAirPressureChart(airPressureData);
                    } else {
                        airPressureResultSection.innerHTML = '';
                    }

                    if (sunshineDurationCheckbox && sunshineDurationCheckbox.checked) {
                        const sunshineDurationData = processSunshineDurationData(data);
                        renderSunshineDurationChart(sunshineDurationData);
                    } else {
                        sunshineDurationResultSection.innerHTML = '';
                    }
                }
            }).catch(error => console.error('Error:', error));
        });
    }

});

function cancelSelection() {
    let checkboxes = document.querySelectorAll('.form-check-input');
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = !checkbox.checked;
    });
}


function selectAllCheckboxes() {
    let checkboxes = document.querySelectorAll('.form-check-input');
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = true;
    });
}

function fetchYourData(selectedValue) {
    const stationSelect = document.querySelector("#city");
    const temperatureCheckbox = document.getElementById("temperature");
    const humidityCheckbox = document.getElementById("humidity");
    const rainfallCheckbox = document.getElementById("rainfall");
    const windSpeedCheckbox = document.getElementById("windSpeed");
    const airPressureCheckbox = document.getElementById("airPressure");
    const sunshineDurationCheckbox = document.getElementById("sunshine");

    if (selectedValue === "選擇縣市") {
        alert("請選擇一個縣市");
        stationSelect.style.border = "1px solid red";
        return Promise.reject(new Error("請選擇一個縣市"));
    } else {
        stationSelect.style.border = "1px solid black";
    }

    let url = `/getSingleMonthData/${selectedValue}`;
    let queryParams = [];
    if (temperatureCheckbox.checked) {
        queryParams.push("type=temperature");
    }
    if (humidityCheckbox.checked) {
        queryParams.push("type=humidity");
    }
    if (rainfallCheckbox.checked) {
        queryParams.push("type=rainfall");
    }
    if (windSpeedCheckbox.checked) {
        queryParams.push("type=windSpeed");
    }
    if (airPressureCheckbox.checked) {
        queryParams.push("type=airPressure");
    }
    if (sunshineDurationCheckbox.checked) {
        queryParams.push("type=sunshine");
    }
    url += queryParams.length > 0 ? '?' + queryParams.join('&') : '';
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('網路異常，請檢查網路');
            }
            return response.json();
        });
}