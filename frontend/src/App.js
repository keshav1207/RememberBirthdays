import React from "react";
import { useState } from "react";

export default function App() {
  const [formValues, setFormValues] = useState({
    firstName: "",
    lastName: "",
    date: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  function recordBirthday(e) {
    e.preventDefault();
    console.log("AAA");
    console.log(formValues);
    setFormValues({
      firstName: "",
      lastName: "",
      date: "",
    });
  }

  return (
    <div>
      <form onSubmit={recordBirthday}>
        <h1> Birthday </h1>
        <label>First Name: </label>
        <input
          name="firstName"
          type="text"
          value={formValues.firstName}
          onChange={handleChange}
        />

        <label>Last Name: </label>
        <input
          name="lastName"
          type="text"
          value={formValues.lastName}
          onChange={handleChange}
        />

        <input
          name="date"
          type="date"
          value={formValues.date}
          onChange={handleChange}
        />

        <button type="submit">Submit</button>
      </form>
    </div>
  );
}
