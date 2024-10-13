defmodule PortfolioWeb.SkillLiveTest do
  use PortfolioWeb.ConnCase

  import Phoenix.LiveViewTest
  import Portfolio.SkillsFixtures

  @create_attrs %{name: "some name", color: "some color"}
  @update_attrs %{name: "some updated name", color: "some updated color"}
  @invalid_attrs %{name: nil, color: nil}

  defp create_skill(_) do
    skill = skill_fixture()
    %{skill: skill}
  end

  describe "Index" do
    setup [:create_skill]

    test "lists all skills", %{conn: conn, skill: skill} do
      {:ok, _index_live, html} = live(conn, ~p"/skills")

      assert html =~ "Listing Skills"
      assert html =~ skill.name
    end

    test "saves new skill", %{conn: conn} do
      {:ok, index_live, _html} = live(conn, ~p"/skills")

      assert index_live |> element("a", "New Skill") |> render_click() =~
               "New Skill"

      assert_patch(index_live, ~p"/skills/new")

      assert index_live
             |> form("#skill-form", skill: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert index_live
             |> form("#skill-form", skill: @create_attrs)
             |> render_submit()

      assert_patch(index_live, ~p"/skills")

      html = render(index_live)
      assert html =~ "Skill created successfully"
      assert html =~ "some name"
    end

    test "updates skill in listing", %{conn: conn, skill: skill} do
      {:ok, index_live, _html} = live(conn, ~p"/skills")

      assert index_live |> element("#skills-#{skill.id} a", "Edit") |> render_click() =~
               "Edit Skill"

      assert_patch(index_live, ~p"/skills/#{skill}/edit")

      assert index_live
             |> form("#skill-form", skill: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert index_live
             |> form("#skill-form", skill: @update_attrs)
             |> render_submit()

      assert_patch(index_live, ~p"/skills")

      html = render(index_live)
      assert html =~ "Skill updated successfully"
      assert html =~ "some updated name"
    end

    test "deletes skill in listing", %{conn: conn, skill: skill} do
      {:ok, index_live, _html} = live(conn, ~p"/skills")

      assert index_live |> element("#skills-#{skill.id} a", "Delete") |> render_click()
      refute has_element?(index_live, "#skills-#{skill.id}")
    end
  end

  describe "Show" do
    setup [:create_skill]

    test "displays skill", %{conn: conn, skill: skill} do
      {:ok, _show_live, html} = live(conn, ~p"/skills/#{skill}")

      assert html =~ "Show Skill"
      assert html =~ skill.name
    end

    test "updates skill within modal", %{conn: conn, skill: skill} do
      {:ok, show_live, _html} = live(conn, ~p"/skills/#{skill}")

      assert show_live |> element("a", "Edit") |> render_click() =~
               "Edit Skill"

      assert_patch(show_live, ~p"/skills/#{skill}/show/edit")

      assert show_live
             |> form("#skill-form", skill: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert show_live
             |> form("#skill-form", skill: @update_attrs)
             |> render_submit()

      assert_patch(show_live, ~p"/skills/#{skill}")

      html = render(show_live)
      assert html =~ "Skill updated successfully"
      assert html =~ "some updated name"
    end
  end
end
