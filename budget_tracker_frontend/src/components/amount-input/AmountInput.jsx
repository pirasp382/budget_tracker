import React, { useEffect, useState } from "react";
import "./AmountInput.css";
import { AiOutlinePlusCircle, AiOutlineMinusCircle } from "react-icons/ai";

const AmountInput = ({ value = null, onChange, setType }) => {
    const [showTooltip, setShowTooltip] = useState(false);
    const [isNegative, setIsNegative] = useState(value < 0);
    const [inputValue, setInputValue] = useState(value !== null ? Math.abs(value).toFixed(2) : "");

    useEffect(() => {
        if (value !== null) {
            setIsNegative(value < 0);
            setInputValue(Math.abs(value).toFixed(2));
            setType(value < 0);
        }
    }, [value, setType]);

    const toggleSign = () => {
        setIsNegative((prev) => {
            const newNegative = !prev;
            const newValue = newNegative ? -parseFloat(inputValue || "0") : parseFloat(inputValue || "0");
            setInputValue(Math.abs(newValue).toFixed(2));
            onChange(newValue);
            setType(newNegative);
            return newNegative;
        });
    };

    const handleChange = (e) => {
        let rawValue = e.target.value.replace(/[^0-9.,]/g, "");
        setInputValue(rawValue);
    };

    const handleBlur = () => {
        let num = parseFloat(inputValue.replace(",", "."));
        if (!isNaN(num)) {
            const finalValue = isNegative ? -num : num;
            setInputValue(Math.abs(finalValue).toFixed(2));
            onChange(finalValue);
            setType(isNegative);
        } else {
            setInputValue("");
        }
    };

    return (
        <div className="amount-input-container">
            <label>Amount:</label>
            <div className="input-wrapper">
                <span className="currency-symbol">€</span>
                <input
                    type="text"
                    value={inputValue !== "" ? (isNegative ? `-${inputValue}` : inputValue) : ""}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    onFocus={(e) => e.target.setSelectionRange(inputValue.length, inputValue.length)}
                    placeholder="0.00"
                    className="amount-input-field"
                    required
                />
                <div
                    className={`icon-container ${isNegative ? "negative" : "positive"}`}
                    onClick={toggleSign}
                    onMouseEnter={() => setShowTooltip(true)}
                    onMouseLeave={() => setShowTooltip(false)}
                >
                    {showTooltip && <div className="tooltip">Use [+] for income and [-] for expenses</div>}
                    {isNegative ? <AiOutlineMinusCircle className="icon red" /> : <AiOutlinePlusCircle className="icon green" />}
                </div>
            </div>
            <p className="amount-type">
                {isNegative ? "This will count as an expense" : "This will count as income"}
            </p>
        </div>
    );
};

export default AmountInput;
