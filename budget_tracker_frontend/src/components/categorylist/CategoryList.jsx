import "./CategoryList.css"
import Category from "../category/Category"


const CategoryList = ({categories}) => {

    const checkHandler = () => null

    return (
        <div className={"liste"}>
            <div className={"list-header categories-liste-header"}>
                <input type={"checkbox"}/>
                {["title", "color", "icon"].map((key) => (
                    <p key={key} className={"sort"}>
                        {key.charAt(0).toUpperCase() + key.slice(1)}
                    </p>
                ))}
            </div>
            {categories.map(item => <Category title={item.title} icon={item.icon} color={item.color}/>)}
        </div>
    )
}

export default CategoryList