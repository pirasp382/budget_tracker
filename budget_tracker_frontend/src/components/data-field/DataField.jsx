import "./DataField.css";
import { FaArrowTrendUp, FaArrowTrendDown, FaPiggyBank } from "react-icons/fa6";

const DataField = ({ title, value }) => {
    const fieldClass = title.toLowerCase(); // "income", "expenses", "balance"
    return (
        <div className={`datafield ${fieldClass}`}>
            <div className="head">
                <h2>{title}</h2>
                <div className="icons">
                    {title === "Income" ? (
                        <FaArrowTrendUp />
                    ) : title === "Expenses" ? (
                        <FaArrowTrendDown />
                    ) : (
                        <FaPiggyBank />
                    )}
                </div>
            </div>
            <h2 className="money">{value}€</h2>
        </div>
    );
};

export default DataField;
