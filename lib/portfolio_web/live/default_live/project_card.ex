defmodule PortfolioWeb.DefaultLive.ProjectCard do
  alias Portfolio.Skills.Skill
  use Phoenix.LiveComponent

  def mount(socket) do
    socket =
      socket
      |> assign(:truncate, true)

    {:ok, socket}
  end

  @spec render(any()) :: Phoenix.LiveView.Rendered.t()
  def render(assigns) do
    ~H"""
    <div class="flex justify-center my-6 card h-96">
      <div class="relative w-56 h-auto transition-all duration-700 delay-75 card__content ">
        <div class="absolute top-0 bottom-0 left-0 right-0 h-full border-8 cursor-pointer bg-yellow-50 tarot-card card__front">
          <h2 class="text-2xl text-black"><%= @card %></h2>
        </div>
        <div class="absolute top-0 bottom-0 left-0 right-0 w-56 border-2 border-black rounded-lg bg-yellow-50 h-fit card__back">
          <div class="flex flex-col items-center w-56 h-80">
            <h1
              class={"font-bold text-lg text-center mt-2 #{if String.length(@project.name) >= 15, do: "cursor-pointer"}"}
              phx-click="toggle_truncate"
              phx-target={@myself}
            >
              <%= if String.length(@project.name) >= 15 and @truncate,
                do: String.slice(@project.name, 0..15) <> "...",
                else: @project.name %>
            </h1>
            <div class="mx-4 overflow-auto">
              <p class="text-lg"><%= @project.description %></p>
            </div>
          </div>
          <div class="flex items-end justify-center h-10 gap-3">
            <%= for skill <- @project.skills do %>
              <span class="px-2 pt-1 mb-1 text-xl rounded-full">
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

  defp get_four_colors(colors), do: colors |> Enum.take(4) |> fill_to_four()

  defp fill_to_four(colors) when length(colors) < 4,
    do: colors ++ (Stream.cycle(colors) |> Enum.take(4 - length(colors)))

  defp fill_to_four(colors), do: colors
end
