defmodule PortfolioWeb.DefaultLive.ExperienceLine do
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <li class="mb-10 ms-4">
      <div class="absolute w-3 h-3 bg-gray-200 rounded-full mt-1.5 -start-1.5 border border-white dark:border-gray-900 dark:bg-gray-700">
      </div>
      <time class="mb-1 text-sm font-normal leading-none text-gray-400 dark:text-gray-500">
        <%= Calendar.strftime(@experience.from, "%B %Y") %>
      </time>
      -
      <time class="mb-1 text-sm font-normal leading-none text-gray-400 dark:text-gray-500">
        <%= if @experience.current_job,
          do: "Current",
          else: Calendar.strftime(@experience.to, "%B %Y") %>
      </time>
      <h3 class="text-lg font-semibold text-gray-900 dark:text-white">
        <%= @experience.position %>
      </h3>
      <p class="mb-4 text-base font-normal text-gray-500 dark:text-gray-400">
        <%= @experience.description %>
      </p>
      <div class="flex gap-3">
        <%= for skill <- @experience.skills do %>
          <span style={get_skill_glow(skill.color)} class="p-0.5 text-white rounded-full ">
            <%= skill.name %>
          </span>
        <% end %>
      </div>
    </li>
    """
  end

  defp get_skill_glow(color) do
    "background-color: #{color};
      box-shadow: 0px 0px 10px 1px #{color};
      "
  end
end
