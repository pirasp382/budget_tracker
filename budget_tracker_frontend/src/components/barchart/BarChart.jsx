import "./BarChart.css"
import Chart from "chart.js/auto"
import {useEffect, useRef, useState} from "react"
import DropDown from "../dropdown/DropDown"
import {LuChartArea, LuChartLine} from "react-icons/lu"
import {FaRegChartBar} from "react-icons/fa"

const change = (type) => type === "bar" ? "bar" : "line"

const chartOptions = {
    line: <span> <LuChartLine/> Line</span>,
    bar: <span><FaRegChartBar/> Bar</span>,
    area: <span><LuChartArea/> Area</span>,
}

const LineChart = ({incomeData, expensesData, history, timeType, setTimetype, timeOptions}) => {
    const chartContainer = useRef(null)
    const chartInstance = useRef(null)
    const [mainChartType, setMainChartType] = useState(chartOptions.line)

    useEffect(() => {
        if ((!incomeData || incomeData.length === 0) && (!expensesData || expensesData.length === 0)) {
            return
        }

        if (chartInstance.current) {
            chartInstance.current.destroy()
        }

        const incomeMap = new Map(incomeData.map(obj => [obj["0"], parseFloat(obj["1"])]))
        const expensesMap = new Map(expensesData.map(obj => [obj["0"], parseFloat(obj["1"])]))
        const allDates = Array.from(new Set([...incomeMap.keys(), ...expensesMap.keys()])).sort()
        const incomeValues = allDates.map(date => incomeMap.get(date) || 0)
        const expensesValues = allDates.map(date => expensesMap.get(date) || 0)
        const historyDates = Object.keys(history).sort()
        const historyValues = historyDates.map(date => history[date])

        console.log(mainChartType)


        chartInstance.current = new Chart(chartContainer.current.getContext("2d"), {
            type: change(mainChartType),
            data: {
                labels: allDates,
                datasets: [
                    {
                        label: "History (€)",
                        data: historyValues,
                        borderColor: "rgba(54, 162, 235, 1)",
                        backgroundColor: "rgba(54, 162, 235, 0.3)",
                        fill: mainChartType === "area",
                        tension: 0.3,
                        yAxisID: "y1",
                    },
                    {
                        label: "Income (€)",
                        data: incomeValues,
                        borderColor: "rgb(55,182,19)",
                        backgroundColor: "rgba(55,182,19,0.3)",
                        fill: mainChartType === "area",
                        tension: 0.3,
                        yAxisID: "y",
                    },
                    {
                        label: "Expenses (€)",
                        data: expensesValues,
                        borderColor: "rgba(255, 99, 132, 1)",
                        backgroundColor: "rgba(255, 99, 132, 0.3)",
                        fill: mainChartType === "area",
                        tension: 0.3,
                        yAxisID: "y",
                    },

                ],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: `Income & Expenses per ${timeType}`,
                        font: {
                            size: 16,
                            weight: "bold",
                        },
                    },
                    tooltip: {
                        callbacks: {
                            label: context => `${context.dataset.label}: ${Math.abs(context.raw).toFixed(2)} €`,
                        },
                    },
                },
                scales: {
                    x: {
                        ticks: {maxRotation: 45, minRotation: 45},
                    },
                    y: {
                        beginAtZero: true,
                        ticks: {callback: value => `${value}€`},
                    },
                    y1: {
                        title: {
                            display: true,
                            align: 'center',
                            text: 'transaction-amount €',
                        },
                        beginAtZero: false,
                        ticks: {callback: value => `${value}€`},
                        position: "right",
                        grid: {drawOnChartArea: false},
                    },
                },
            },
        })

        return () => chartInstance.current.destroy()
    }, [incomeData, expensesData, history, mainChartType])

    const handleTime=(value)=>{
        setTimetype(value)
    }

    return (
        <div id="graphen">
            <canvas ref={chartContainer}></canvas>
            <DropDown saveSelected={handleTime} selectOptions={timeOptions} defaultValue={timeOptions.day}/>
            <DropDown saveSelected={setMainChartType} selectOptions={chartOptions}
                      defaultValue={chartOptions.line}/>
        </div>
    )
}

export default LineChart
