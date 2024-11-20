defmodule PortfolioWeb.DefaultLive.SkillItem do
  alias Phoenix.LiveView.JS
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <span
      id={"skill-#{@skill.id}"}
      class={"p-1 transition ease-out rounded-full cursor-pointer hover:scale-110 #{elixir?(@skill.name)} #{selected?(@selected)}"}
      style={"margin-#{@margin}: #{get_get_margin_percentage(@index, @half_length)}%;"}
      phx-click="add_skill_filter"
      phx-value-id={@skill.id}
      phx-hook="SkillItem"
    >
      <%= @skill.name %>
    </span>
    """
  end

  defp get_get_margin_percentage(index, half_length), do: (10 - abs(index - half_length / 2)) * 5

  defp selected?(selected) do
    if selected, do: "font-bold text-2xl", else: "text-xl"
  end

  defp elixir?(skill) do
    if String.downcase(skill) == "elixir", do: "animate-pulse font-bold", else: ""
  end
end
