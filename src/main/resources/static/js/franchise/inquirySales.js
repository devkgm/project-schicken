import {getSalesList,getSalesPerWeeks,getSalesPerMonth,getSalesPerDays,getSalesDays, getSellDays} from "../api/sales.js";
const salesListContainer = document.getElementById("salesListContainer");
const franchiseId = document.getElementById("main").dataset.id;

Array.prototype.slice.call(document.querySelector("select"))
    .forEach(options => {
        if(options.value == '${pager.kind}') options.selected = true;
    });
async function loadSales(){
    const result = await getSalesList(articlePage, franchiseId);
    renderTable(result.data);
}
async function renderTable(data){
    let innerHtml = "";
    data.forEach((sale) => {
        let menus = sale.details.map(item =>
            item.menu.menu+"("+item.menu.price.toLocaleString("ko-KR")+")"
        )
        innerHtml += `
                <tr>
                    <td>${sale.id}</td>
                    <td class="text-wrap">${menus.join(", ")}</td>
                    <td>${sale.price.toLocaleString("ko-KR")}원</td>
                    <td>${dayjs(sale.salesDate).format("YYYY-MM-DD HH:mm")}</td>
                </tr>
            `;
    })
    salesListContainer.insertAdjacentHTML("beforeend", innerHtml);
    $articleEnd = salesListContainer.children[salesListContainer.children.length - 2];
    if (data.length < 10) return;
    articleObserver.observe($articleEnd);
}
let options = {
    threshold: 0,
};
let articlePage = 1;
const $articleResult = document.querySelector("#salesListContainer");
let $articleEnd;
const articleCallback = (entries, observer) => {
    entries.forEach(async (entry) => {
        if (entry.isIntersecting) {
            articlePage++;
            observer.unobserve($articleEnd);
            loadSales();
        }
    });
};
const articleObserver = new IntersectionObserver(articleCallback, options);
(async function(){
    await loadSales();
})();

const chartFilter = document.querySelectorAll("#salesChartContainer .dropdown-item");
[...chartFilter].forEach(el => {
    el.onclick = (event) => {
        handleFilterClick(event)
    }
})
async function loadSalesPerDays(){
    const result = await getSalesPerDays(franchiseId);
    return result.data
}
async function loadSalesPerWeeks(){
    const result = await getSalesPerWeeks(franchiseId);
    return result.data
}
async function loadSalesPerMonth(){
    const result = await getSalesPerMonth(franchiseId);
    return result.data
}
async function loadSalesDays(){
    const result = await getSalesDays(franchiseId);
    return result.data
}
async function loadSellDays(){
    const result = await getSellDays(franchiseId);
    return result.data
}

let chart
document.addEventListener("DOMContentLoaded", async () => {
    const data = await loadSalesPerDays();
    const sales = await loadSalesDays();
    const sell = await loadSellDays();
    const series = data.map(d => {
        return {
            x: new Date(d.salesDate),
            y: d.price
        }
    });
    chart = new ApexCharts(document.querySelector("#reportsChart"), {
        series: [{
            name:"매출",
            data: series
        }],
        chart: {
            height: 350,
            type: 'area',
            toolbar: {
                show: false
            },
            defaultLocale: 'ko',
            locales: [{
                name: 'ko',
                options: {
                    months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                    shortMonths: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                    days: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
                    shortDays: ['일', '월', '화', '수', '목', '금', '토'],
                    toolbar: {
                        download: 'SVG로 다운로드',
                        selection: '선택',
                        selectionZoom: '선택 확대',
                        zoomIn: '확대',
                        zoomOut: '축소',
                        pan: '이동',
                        reset: '확대/축소 초기화',
                    }
                }
            }]
        },
        markers: {
            size: 4
        },
        colors: ['#4154f1', '#2eca6a', '#ff771d'],
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                opacityFrom: 0.3,
                opacityTo: 0.4,
                stops: [0, 90, 100]
            }
        },
        dataLabels: {
            enabled: false
        },
        stroke: {
            curve: 'smooth',
            width: 2
        },
        xaxis: {
            type: 'datetime',
            labels: {
                datetimeFormatter: {
                    year: 'yyyy',
                    month: 'yy MMM',
                    day: 'MMM dd일'
                },
            },
        },
        yaxis: {
            labels: {
                formatter: function (val) {
                    return new Intl.NumberFormat('en-US', {
                        style: 'currency',
                        currency: 'KRW'
                    }).format(val);
                }
            }
        },
        tooltip: {
            x: {
                format: 'yy/MM/dd'
            },
        }
    })
    chart.render();

    document.getElementById("totalSales").innerText = sales.totalSales.toLocaleString("ko-KR") + "원";
    document.getElementById("totalSell").innerText = sell.totalSell;
    document.getElementById("salesIncreasePercent").innerText = Math.floor(sales.increase / sales.totalSales * 100) +"%";
    document.getElementById("sellIncreasePercent").innerText = Math.floor(sell.increase / sell.totalSell * 100) + "%";
    if(sales.increase > 0){
        document.getElementById("salesIncreaseType").innerText = "증가";
    } else {
        document.getElementById("salesIncreaseType").innerText = "감소";
    }
    if(sell.increase > 0){
        document.getElementById("sellIncreaseType").innerText = "증가";
    } else {
        document.getElementById("sellIncreaseType").innerText = "감소";
    }
})
async function handleFilterClick(event) {
    event.preventDefault();
    console.log(event)
    const perUnit = document.getElementById("perUnit");
    const filterValue = event.target.getAttribute('data-value');
    let data = []
    switch(filterValue){
        case 'days':
            data = await loadSalesPerDays();
            perUnit.innerText = "/일";
            break;
        case 'weeks':
            data =  await loadSalesPerWeeks();
            perUnit.innerText = "/주";
            break;
        case'month':
            data = await loadSalesPerMonth();
            perUnit.innerText = "/월";
            break;
    }
    const series = data.map(d => {
        return {
            x: new Date(d.salesDate),
            y: d.price
        }
    });
    chart.updateSeries([{
        data: series
    }]);
}