import { createElement, useEffect, useRef } from "react";
import useFetch from "../../hooks/useFetch";
import Category from "../../model/Category";

export default function CategorySelect() {
  const selectRef = useRef<HTMLSelectElement>(null);
  const { data, isPending, requestStatus, error } = useFetch({
    url: "http://localhost:8082/downloaderservice/api/category",
  });

  let categorys: Array<Category> | null = null;
  useEffect(() => {
    if (isPending) {
      return;
    }
    categorys = data._embedded.categorys;
    if (!categorys) {
      return;
    }
    createOption(categorys);
  }, [isPending]);

  /**Creamos una funcion que  retorna un arreglo de options*/
  const createOption = (categorys: Array<Category>) => {
    categorys.forEach((category: Category) => {
      if (!selectRef.current) {
        return;
      }
      const option: HTMLOptionElement = document.createElement("option");
      option.text = category.categoryName;
      option.value = `${category.id}`;
      if (category.id == 1) {
        option.selected = true;
      }
      selectRef.current.add(option);
    });
  };
  return (
    <select
      ref={selectRef}
      className="form-select"
      aria-label=".form-select-lg example"
    >
    </select>
  );
}
