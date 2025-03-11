import "./Category.css"

const Category = ({title, color, icon}) => {
    return (
        <div className={"category"}>
            <input type="checkbox" checked={false} onChange={() => {
            }}/>
            <div className={"category-inner inner"}>
                <p>{title}</p>
                <p style={{color: color}} >{color}</p>
                <p>{icon}</p>
            </div>
        </div>
    )
}

export default Category