defmodule PortfolioWeb.DefaultLive.SkillItem do
  alias Phoenix.LiveView.JS
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <span
      id={"skill-#{@skill.id}"}
      class={"p-1 transition ease-out rounded-full cursor-pointer hover:scale-110 #{if @skill.name == "Elixir", do: "animate-pulse font-bold"}"}
      style={"margin-#{@margin}: #{get_get_margin_percentage(@index, @half_length)}%; #{get_skill_glow(@selected, @skill.color)}"}
      phx-click={
        JS.push("add_skill_filter")
        |> JS.toggle_class(
          " shadow-2xl text-white  #{if @skill.name == "Elixir", do: "animate-pulse"}"
        )
      }
      phx-value-id={@skill.id}
    >
      <%= @skill.name %>
    </span>
    """
  end

  defp get_get_margin_percentage(index, half_length), do: (10 - abs(index - half_length / 2)) * 5

  defp get_skill_glow(selected, color) do
    if selected do
      "background-color: #{color};
      box-shadow: 0px 0px 41px 17px #{color};
      "
    end
  end
end
