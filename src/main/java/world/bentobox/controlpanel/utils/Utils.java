//
// Created by BONNe
// Copyright - 2019
//


package world.bentobox.controlpanel.utils;


import org.bukkit.World;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.user.User;


public class Utils
{
	/**
	 * This method gets string value of given permission prefix. If user does not have
	 * given permission or it have all (*), then return default value.
	 * @param user User who's permission should be checked.
	 * @param permissionPrefix Prefix that need to be found.
	 * @param defaultValue Default value that will be returned if permission not found.
	 * @return String value that follows permissionPrefix.
	 */
	public static String getPermissionValue(User user, String permissionPrefix, String defaultValue)
	{
		if (user.isPlayer())
		{
			if (permissionPrefix.endsWith("."))
			{
				permissionPrefix = permissionPrefix.substring(0, permissionPrefix.length() - 1);
			}

			String permPrefix = permissionPrefix + ".";

			List<String> permissions = user.getEffectivePermissions().stream().
				map(PermissionAttachmentInfo::getPermission).
				filter(permission -> permission.startsWith(permPrefix)).
				collect(Collectors.toList());

			for (String permission : permissions)
			{
				if (permission.contains(permPrefix + "*"))
				{
					// * means all. So continue to search more specific.
					continue;
				}

				String[] parts = permission.split(permPrefix);

				if (parts.length > 1)
				{
					return parts[1];
				}
			}
		}

		return defaultValue;
	}


	/**
	 * This method transforms given World into GameMode name. If world is not a GameMode
	 * world then it returns null.
	 * @param world World which gameMode name must be found out.
	 * @return GameMode name or null.
	 */
	public static String getGameMode(World world)
	{
		return BentoBox.getInstance().getIWM().getAddon(world).
			map(gameModeAddon -> gameModeAddon.getDescription().getName()).
			orElse("");
	}


	/**
	 * This method transforms given GameMode into name.
	 * @param gameModeAddon GameMode which name must be returned.
	 * @return GameMode name.
	 */
	public static String getGameMode(GameModeAddon gameModeAddon)
	{
		return gameModeAddon.getDescription().getName();
	}


	/**
	 * This method allows to get next value from array list after given value.
	 * @param values Array that should be searched for given value.
	 * @param currentValue Value which next element should be found.
	 * @param <T> Instance of given object.
	 * @return Next value after currentValue in values array.
	 */
	public static <T> T getNextValue(T[] values, T currentValue)
	{
		for (int i = 0; i < values.length; i++)
		{
			if (values[i].equals(currentValue))
			{
				if (i + 1 == values.length)
				{
					return values[0];
				}
				else
				{
					return values[i + 1];
				}
			}
		}

		return currentValue;
	}


	/**
	 * This method allows to get previous value from array list after given value.
	 * @param values Array that should be searched for given value.
	 * @param currentValue Value which previous element should be found.
	 * @param <T> Instance of given object.
	 * @return Previous value before currentValue in values array.
	 */
	public static <T> T getPreviousValue(T[] values, T currentValue)
	{
		for (int i = 0; i < values.length; i++)
		{
			if (values[i].equals(currentValue))
			{
				if (i > 0)
				{
					return values[i - 1];
				}
				else
				{
					return values[values.length - 1];
				}
			}
		}

		return currentValue;
	}

	/**
	 * Reads a list of objects and converts it into an array of integers.
	 * <p>
	 * The method processes each element in the list:
	 * <ul>
	 *     <li>If the element is an Integer, it adds it directly to the result array.</li>
	 *     <li>If the element is a String in the format "start-end", it adds all integers
	 *         from "start" to "end" (inclusive) to the result array.</li>
	 * </ul>
	 *
	 * <b>Example:</b>
	 * If the input list is `["1-3", 5, "7-9"]`, the method will return an array
	 * containing `[1, 2, 3, 5, 7, 8, 9]`.
	 *
	 * @param objectList A list of objects, each of which can either be an Integer or a String
	 *                  in the format "start-end" (inclusive range).
	 * @return An array of integers containing all the integers parsed from the input list.
	 *         The integers from ranges are added to the result array in order.
	 * @throws NumberFormatException If the string representation of a number is not valid.
	 */
	public static int[] readIntArray(List<?> objectList) {
		List<Integer> values = new ArrayList<>();

		// Process each item in the objectList
		for (Object o : objectList) {
			// If the object is an Integer, add it directly to values
			if (o instanceof Integer) {
				values.add((int) o);
			}
			// If the object is a String in the form "start-end"
			else if (o instanceof String) {
				try {
					int n = Integer.parseInt((String) o);
					values.add(n);
				}catch (NumberFormatException ignored) {}
				String[] args = ((String) o).split("-");
				if (args.length >= 2) {
					try {
						int n0 = Integer.parseInt(args[0]);
						int n1 = Integer.parseInt(args[1]) + 1; // Add 1 to include the upper bound
						// Add all integers in the range [n0, n1)
						for (int n : IntStream.range(n0, n1).toArray()) {
							values.add(n);
						}
						continue;
					} catch (NumberFormatException e) {
						// Handle invalid number format if necessary
						throw new NumberFormatException("Invalid number format in range string: " + o);
					}
				}
			}
		}

		// Convert List<Integer> to a primitive int array and return it
		return values.stream().mapToInt(Integer::intValue).toArray();
	}
}
