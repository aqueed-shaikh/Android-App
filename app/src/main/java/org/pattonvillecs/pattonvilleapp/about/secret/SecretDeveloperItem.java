/*
 * Copyright (C) 2017  Mitchell Skaggs, Keturah Gadson, Ethan Holtgrieve, and Nathan Skelton
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.pattonvillecs.pattonvilleapp.about.secret;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.DrawableRes;

import org.pattonvillecs.pattonvilleapp.R;
import org.pattonvillecs.pattonvilleapp.about.DeveloperHeaderItem;
import org.pattonvillecs.pattonvilleapp.about.DeveloperItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by skaggsm on 5/12/17.
 */

public class SecretDeveloperItem extends DeveloperItem {
    public SecretDeveloperItem(DeveloperHeaderItem headerItem, String name, String text, @DrawableRes int imageRes) {
        super(headerItem, name, text, imageRes);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DeveloperViewHolder holder, int position, List payloads) {
        Context context = adapter.getRecyclerView().getContext();

        holder.image.setImageDrawable(null);
        holder.image.setBackground(null);

        holder.view.setBackground(null);
        holder.view.setOnClickListener(null);

        holder.name.setText(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.view.setBackgroundResource(context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless}).getResourceId(0, -1));
        } else {
            holder.view.setBackgroundResource(context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackground}).getResourceId(0, -1));
        }

        holder.view.setOnLongClickListener(v -> {
            context.startActivity(new Intent(context, SecretActivity.class));
            return true;
        });
    }
}
