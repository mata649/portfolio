defmodule PortfolioWeb.DefaultLive.SkillItem do
  alias Phoenix.LiveView.JS
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <span
      id={"skill-#{@skill.id}"}
      class={"p-1 transition ease-out rounded-full text-3xl cursor-pointer hover:scale-110 #{if @skill.name == "Elixir", do: "animate-pulse font-bold"}"}
      style={"margin-#{@margin}: #{get_get_margin_percentage(@index, @half_length)}%; #{get_skill_glow(@selected, @skill.color)}"}
      phx-click={
        JS.push("add_skill_filter")
        |> JS.toggle_class(
          " shadow-2xl text-white  #{if @skill.name == "Elixir", do: "animate-pulse"}"
        )
      }
      phx-value-id={@skill.id}
      phx-hook="SkillItem"
    >
      <%= @skill.name %>
    </span>
    """
  end

  defp get_get_margin_percentage(index, half_length), do: (10 - abs(index - half_length / 2)) * 5

  defp get_skill_glow(selected, color) do
    if selected do
    "color: #{color};
    text-shadow: 0 0 5px #{color}, 0 0 10px #{color}, 0 0 20px #{color}, 0 0 40px #{color};"
    end
  end
end
