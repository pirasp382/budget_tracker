import { useEffect, useRef } from "react";
import Chart from "chart.js/auto";
import "./DonutChart.css";

const DonutChart = ({ categoryData }) => {
    const chartRef = useRef(null);
    const chartInstance = useRef(null);

    useEffect(() => {
        if (!categoryData || categoryData.length === 0) {
            return;
        }

        if (chartInstance.current) {
            chartInstance.current.destroy();
        }

        const labels = categoryData.map(obj => obj["0"]);
        const values = categoryData.map(obj => parseFloat(obj["1"]));

        chartInstance.current = new Chart(chartRef.current, {
            type: "doughnut",
            data: {
                labels: labels,
                datasets: [
                    {
                        data: values,
                        backgroundColor: ["#36A2EB", "#FF6384", "#FFCE56", "#4BC0C0"],
                        hoverOffset: 4,
                    },
                ],
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: "Category-Transactions",
                        font: { size: 16, weight: "bold" },
                    },
                    legend: {
                        position: "bottom",
                    },
                },
            },
        });

        return () => {
            if (chartInstance.current) {
                chartInstance.current.destroy();
            }
        };
    }, [categoryData]);

    return (
        <div className="category-chart">
            <canvas ref={chartRef}></canvas>
        </div>
    );
};

export default DonutChart;
