defmodule Portfolio.ExperiencesTest do
  use Portfolio.DataCase

  alias Portfolio.Experiences

  describe "experiences" do
    alias Portfolio.Experiences.Experience

    import Portfolio.ExperiencesFixtures
    import Portfolio.SkillsFixtures

    @invalid_attrs %{
      position: nil,
      description: nil,
      location: nil,
      to: nil,
      from: nil,
      company: nil,
      currentJob: nil
    }
    @skills_not_loaded %Ecto.Association.NotLoaded{
      __field__: :skills,
      __cardinality__: :many,
      __owner__: Portfolio.Experiences.Experience
    }

    test "list_experiences/0 returns all experiences without skills" do
      skill = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})

      assert Experiences.list_experiences() == [
               %Experience{experience | skills: @skills_not_loaded}
             ]
    end

    test "list_experiences/1 returns all experiences with skills" do
      skill = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})
      assert Experiences.list_experiences([:skills]) == [experience]
    end

    test "get_experience!/1 returns the experience with given id without skills" do
      skill = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})
      assert Experiences.get_experience!(experience.id, [:skills]) == experience
    end

    test "get_experience!/2 returns the experience with given id with skills" do
      skill = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})
      assert Experiences.get_experience!(experience.id, [:skills]) == experience
    end

    test "create_experience/1 with valid data creates a experience" do
      skill = skill_fixture()

      valid_attrs = %{
        position: "some position",
        description: "some description",
        location: "some location",
        to: ~D[2024-10-20],
        from: ~D[2024-10-20],
        company: "some company",
        current_job: false,
        skills: [skill.id]
      }

      assert {:ok, %Experience{} = experience} = Experiences.create_experience(valid_attrs)
      assert experience.position == "some position"
      assert experience.description == "some description"
      assert experience.location == "some location"
      assert experience.to == ~D[2024-10-20]
      assert experience.from == ~D[2024-10-20]
      assert experience.company == "some company"
      assert experience.current_job == false
    end

    test "create_experience/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Experiences.create_experience(@invalid_attrs)
    end

    test "update_experience/2 with valid data updates the experience" do
      skill = skill_fixture()
      skill2 = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})

      update_attrs = %{
        position: "some updated position",
        description: "some updated description",
        location: "some updated location",
        to: ~D[2024-10-21],
        from: ~D[2024-10-21],
        company: "some updated company",
        current_job: false,
        skills: [skill2.id]
      }

      assert {:ok, %Experience{} = experience} =
               Experiences.update_experience(experience, update_attrs)

      assert experience.position == "some updated position"
      assert experience.description == "some updated description"
      assert experience.location == "some updated location"
      assert experience.to == ~D[2024-10-21]
      assert experience.from == ~D[2024-10-21]
      assert experience.company == "some updated company"
      assert experience.current_job == false
    end

    test "update_experience/2 with invalid data returns error changeset" do
      skill = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})

      assert {:error, %Ecto.Changeset{}} =
               Experiences.update_experience(experience, @invalid_attrs)

      assert experience == Experiences.get_experience!(experience.id, [:skills])
    end

    test "delete_experience/1 deletes the experience" do
      skill = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})
      assert {:ok, %Experience{}} = Experiences.delete_experience(experience)
      assert_raise Ecto.NoResultsError, fn -> Experiences.get_experience!(experience.id) end
    end

    test "change_experience/1 returns a experience changeset" do
      skill = skill_fixture()
      experience = experience_fixture(%{skills: [skill.id]})
      assert %Ecto.Changeset{} = Experiences.change_experience(experience)
    end
  end
end
