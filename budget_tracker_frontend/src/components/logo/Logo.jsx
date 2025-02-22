import logo from "./logo.webp"
import "./Logo.css"

const Logo=()=>{
    return(
        <div className={"logo"}>
            <img src={logo} alt="logo_image"/>
            <div className={"name"}>FinanceMaster</div>
        </div>
    )
}

export default Logo;