import { useRef } from "react";
import { useNavigate } from "react-router-dom";
import "./MainRoute.css";
const Main = () => {
  const navigate = useNavigate();
  return (
    <div class="root">
      <div class="search__container">
        <p class="search__title">Go ahead, hover over search</p>
        <input
          class="search__input"
          type="text"
          placeholder="Search"
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              navigate(e.target.value);
            }
          }}
        />
      </div>

      <div class="credits__container">
        <p class="credits__text">
          Background color: Pantone Color of the Year 2016{" "}
          <a
            href="https://www.pantone.com/color-of-the-year-2016"
            class="credits__link"
          >
            Rose Quartz
          </a>
        </p>
      </div>
    </div>
  );
};

export default Main;
