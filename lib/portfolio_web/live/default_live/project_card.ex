defmodule PortfolioWeb.DefaultLive.ProjectCard do
  alias Phoenix.LiveView.JS
  alias Portfolio.Skills.Skill
  use Phoenix.LiveComponent

  def mount(socket) do
    socket =
      socket
      |> assign(:truncate, true)

    {:ok, socket}
  end

  def render(assigns) do
    ~H"""
    <div
      class="flex justify-center my-6 card h-96"
      phx-remove={JS.hide(transition: {"ease-out duration-75", "opacity-100", "opacity-0"}, time: 75)}
      style="display: none"
      phx-mounted={JS.show(transition: {"ease-in duration-75", "opacity-0", "opacity-100"}, time: 75)}
    >
      <div class="relative w-56 h-auto transition-all duration-700 delay-75 card__content ">
        <div class="absolute top-0 bottom-0 left-0 right-0 cursor-pointer card__front">
          <img
            class="h-96"
            src={"images/tarot/#{@card}.png"}
            style={get_tarot_glow_color(@project.skills)}
          />
        </div>
        <div
          class="absolute top-0 bottom-0 left-0 right-0 w-56 text-white bg-slate-900 h-fit card__back"
          style={get_tarot_glow_color(@project.skills)}
        >
          <div class="flex flex-col items-center w-56 h-80">
            <h1
              class={"font-bold text-2xl text-center mt-2 #{if String.length(@project.name) >= 15, do: "cursor-pointer"}"}
              phx-click="toggle_truncate"
              phx-target={@myself}
            >
              <%= if String.length(@project.name) >= 15 and @truncate,
                do: String.slice(@project.name, 0..15) <> "...",
                else: @project.name %>
            </h1>
            <div class="mx-4 overflow-auto text-lg">
              <p class="text-xl"><%= @project.description %></p>

            </div>
          </div>
          <div class="flex items-end justify-center h-10 gap-3">
            <%= for skill <- @project.skills do %>
              <span
                class="px-2 pt-1 mb-1 text-xl rounded-full "
                style={get_skill_glow(skill.color)}
              >
                <%= skill.name %>
              </span>
            <% end %>
          </div>
          <div class="flex justify-center h-6">
            <.link
              href={@project.githubURL}
              target="_blank"
              class="font-bold delay-75 hover:scale-105"
            >
              See in GitHub
            </.link>
          </div>
        </div>
      </div>
    </div>
    """
  end

  def handle_event("toggle_truncate", _unsigned_params, socket) do
    socket = socket |> assign(:truncate, !socket.assigns.truncate)
    {:noreply, socket}
  end

  defp get_tarot_glow_color(skills) do
    colors = skills |> Enum.map(fn %Skill{color: color} -> color end) |> get_four_colors()

    "box-shadow: 0 0 5px #{colors |> Enum.at(0)}, 0 0 10px  #{colors |> Enum.at(1)}, 0 0 20px  #{colors |> Enum.at(2)}, 0 0 40px  #{colors |> Enum.at(3)};"
  end

  defp get_skill_glow(color) do
    "color: #{color};
    text-shadow: 0 0 5px #{color}, 0 0 10px #{color}, 0 0 20px #{color}, 0 0 40px #{color};"
  end

  defp get_four_colors(colors), do: colors |> Enum.take(4) |> fill_to_four()

  defp fill_to_four(colors) when length(colors) < 4,
    do: colors ++ (Stream.cycle(colors) |> Enum.take(4 - length(colors)))

  defp fill_to_four(colors), do: colors
end
