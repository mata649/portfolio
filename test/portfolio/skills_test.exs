defmodule Portfolio.SkillsTest do
  use Portfolio.DataCase

  alias Portfolio.Skills

  describe "skills" do
    alias Portfolio.Skills.Skill

    import Portfolio.SkillsFixtures

    @invalid_attrs %{name: nil, color: nil}

    test "list_skills/0 returns all skills" do
      skill = skill_fixture()
      assert Skills.list_skills() == [skill]
    end

    test "get_skill!/1 returns the skill with given id" do
      skill = skill_fixture()
      assert Skills.get_skill!(skill.id) == skill
    end

    test "create_skill/1 with valid data creates a skill" do
      valid_attrs = %{name: "some name", color: "some color"}

      assert {:ok, %Skill{} = skill} = Skills.create_skill(valid_attrs)
      assert skill.name == "some name"
      assert skill.color == "some color"
    end

    test "create_skill/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Skills.create_skill(@invalid_attrs)
    end

    test "update_skill/2 with valid data updates the skill" do
      skill = skill_fixture()
      update_attrs = %{name: "some updated name", color: "some updated color"}

      assert {:ok, %Skill{} = skill} = Skills.update_skill(skill, update_attrs)
      assert skill.name == "some updated name"
      assert skill.color == "some updated color"
    end

    test "update_skill/2 with invalid data returns error changeset" do
      skill = skill_fixture()
      assert {:error, %Ecto.Changeset{}} = Skills.update_skill(skill, @invalid_attrs)
      assert skill == Skills.get_skill!(skill.id)
    end

    test "delete_skill/1 deletes the skill" do
      skill = skill_fixture()
      assert {:ok, %Skill{}} = Skills.delete_skill(skill)
      assert_raise Ecto.NoResultsError, fn -> Skills.get_skill!(skill.id) end
    end

    test "change_skill/1 returns a skill changeset" do
      skill = skill_fixture()
      assert %Ecto.Changeset{} = Skills.change_skill(skill)
    end
  end
end
