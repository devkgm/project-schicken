import {getWeather, getWeatherList} from "/js/api/weather.js";
async function loadWeather(){
    const result = await getWeather();
    const nowTemp = document.getElementById("nowTemp");
    const weatherIcon = document.getElementById("weatherIcon");
    const weatherDescription = document.getElementById("weatherDescription");
    nowTemp.innerText = Math.floor(result.main.temp) +"°";
    weatherIcon.src="/img/weather/"+result.weather[0].icon+".png";
    weatherDescription.innerText = result.weather[0].description;
    timeWeather.innerHTML = `
                <div class="d-flex flex-column text-nowrap align-items-center now">
                    <span>지금</span>
                    <div style="height: 32px;">
                        <img src="/img/weather/${result.weather[0].icon}.png" width="30" alt="icon"/>
                    </div>
                    <div>
                        <span class="">${Math.floor(result.main.temp)}°</span>
                    </div>
                </div>
            `
    const now = timeWeather.querySelector("div.now")
    now.addEventListener("click",function (){
        renderDetail(result)
        changeBackground(result.weather[0].icon.indexOf("d") == -1);
    })
    renderDetail(result)
    changeBackground(result.weather[0].icon.indexOf("d") == -1);
}
const timeWeather = document.getElementById("timeWeather");
async function loadWeatherList(){
    const result = await getWeatherList();
    weatherDatas = result;
    let innerHtml ="";
    result.forEach((data,index) => {
        innerHtml += `
                <div class="d-flex flex-column text-nowrap align-items-center" data-index="${index}">
                    <span>${unixTo24Hour(data.dt)}</span>
                    <div style="height: 32px;">
                        <img src="/img/weather/${data.weather[0].icon}.png" width="30" alt="icon"/>
                    </div>
                    <div>
                        <span class="">${Math.floor(data.main.temp)}°</span>
                    </div>
                </div>
            `
    })
    timeWeather.insertAdjacentHTML("beforeend", innerHtml);
    [...timeWeather.querySelectorAll("[data-index]")].forEach(el => {
        el.addEventListener("click", (e) => {
            let index = e.currentTarget.getAttribute("data-index")
            renderDetail(weatherDatas[index]);
        })
    })
}
let weatherDatas = [];
function renderDetail(data){
    const detailWeather = document.getElementById("detailWeather")
    let innerHtml = "";
    const detailWind = document.getElementById("detailWind");
    const detailRain = document.getElementById("detailRain");
    const detailHumidity = document.getElementById("detailHumidity");
    const realTemp = document.getElementById("realTemp");
    detailWind.innerText = data.wind.speed + "m/s";
    detailRain.innerText = data.rain ? data.rain["1h"] ? data.rain["1h"] : data.rain["3h"] : "0";
    detailRain.innerText += "mm"
    detailHumidity.innerText = data.main.humidity + "%";
    realTemp.innerText = Math.floor(data.main.feels_like) + "°";
}
;
document.addEventListener("DOMContentLoaded", async function(){
    const weatherLoading = document.getElementById("weatherLoading")
    const weatherMain = document.getElementById("weatherMain");
    await loadWeather();
    await loadWeatherList()
    weatherLoading.classList.add("d-none")
    weatherMain.classList.remove("d-none")
})

function unixTo24Hour(unixTimestamp) {
    var date = new Date(unixTimestamp * 1000);
    var hours = date.getHours();
    return hours+"시"
}

function changeBackground(night){
    const weatherContainer = document.querySelector(".weather-container");
    if(night === true){
        weatherContainer.classList.add("night");
        weatherContainer.classList.remove("day");
    } else {
        weatherContainer.classList.remove("night");
        weatherContainer.classList.add("day");
    }
}