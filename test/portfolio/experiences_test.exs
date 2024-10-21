defmodule Portfolio.ExperiencesTest do
  use Portfolio.DataCase

  alias Portfolio.Experiences

  describe "experiences" do
    alias Portfolio.Experiences.Experience

    import Portfolio.ExperiencesFixtures

    @invalid_attrs %{position: nil, description: nil, location: nil, to: nil, from: nil, company: nil, currentJob: nil}

    test "list_experiences/0 returns all experiences" do
      experience = experience_fixture()
      assert Experiences.list_experiences() == [experience]
    end

    test "get_experience!/1 returns the experience with given id" do
      experience = experience_fixture()
      assert Experiences.get_experience!(experience.id) == experience
    end

    test "create_experience/1 with valid data creates a experience" do
      valid_attrs = %{position: "some position", description: "some description", location: "some location", to: ~D[2024-10-20], from: ~D[2024-10-20], company: "some company", currentJob: true}

      assert {:ok, %Experience{} = experience} = Experiences.create_experience(valid_attrs)
      assert experience.position == "some position"
      assert experience.description == "some description"
      assert experience.location == "some location"
      assert experience.to == ~D[2024-10-20]
      assert experience.from == ~D[2024-10-20]
      assert experience.company == "some company"
      assert experience.currentJob == true
    end

    test "create_experience/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Experiences.create_experience(@invalid_attrs)
    end

    test "update_experience/2 with valid data updates the experience" do
      experience = experience_fixture()
      update_attrs = %{position: "some updated position", description: "some updated description", location: "some updated location", to: ~D[2024-10-21], from: ~D[2024-10-21], company: "some updated company", currentJob: false}

      assert {:ok, %Experience{} = experience} = Experiences.update_experience(experience, update_attrs)
      assert experience.position == "some updated position"
      assert experience.description == "some updated description"
      assert experience.location == "some updated location"
      assert experience.to == ~D[2024-10-21]
      assert experience.from == ~D[2024-10-21]
      assert experience.company == "some updated company"
      assert experience.currentJob == false
    end

    test "update_experience/2 with invalid data returns error changeset" do
      experience = experience_fixture()
      assert {:error, %Ecto.Changeset{}} = Experiences.update_experience(experience, @invalid_attrs)
      assert experience == Experiences.get_experience!(experience.id)
    end

    test "delete_experience/1 deletes the experience" do
      experience = experience_fixture()
      assert {:ok, %Experience{}} = Experiences.delete_experience(experience)
      assert_raise Ecto.NoResultsError, fn -> Experiences.get_experience!(experience.id) end
    end

    test "change_experience/1 returns a experience changeset" do
      experience = experience_fixture()
      assert %Ecto.Changeset{} = Experiences.change_experience(experience)
    end
  end
end
