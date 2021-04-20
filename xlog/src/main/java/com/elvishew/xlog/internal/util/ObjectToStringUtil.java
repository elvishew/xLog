/*
 * Copyright 2016 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elvishew.xlog.internal.util;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * Utility for formatting object to string.
 */
public class ObjectToStringUtil {

  /**
   * Bundle object to string, the string would be in the format of "Bundle[{...}]".
   */
  public static String bundleToString(Bundle bundle) {
    if (bundle == null) {
      return "null";
    }

    StringBuilder b = new StringBuilder(128);
    b.append("Bundle[{");
    bundleToShortString(bundle, b);
    b.append("}]");
    return b.toString();
  }

  /**
   * Intent object to string, the string would be in the format of "Intent { ... }".
   */
  public static String intentToString(Intent intent) {
    if (intent == null) {
      return "null";
    }

    StringBuilder b = new StringBuilder(128);
    b.append("Intent { ");
    intentToShortString(intent, b);
    b.append(" }");
    return b.toString();
  }

  private static void bundleToShortString(Bundle bundle, StringBuilder b) {
    boolean first = true;
    for (String key : bundle.keySet()) {
      if (!first) {
        b.append(", ");
      }
      b.append(key).append('=');
      Object value = bundle.get(key);
      if (value instanceof int[]) {
        b.append(Arrays.toString((int[]) value));
      } else if (value instanceof byte[]) {
        b.append(Arrays.toString((byte[]) value));
      } else if (value instanceof boolean[]) {
        b.append(Arrays.toString((boolean[]) value));
      } else if (value instanceof short[]) {
        b.append(Arrays.toString((short[]) value));
      } else if (value instanceof long[]) {
        b.append(Arrays.toString((long[]) value));
      } else if (value instanceof float[]) {
        b.append(Arrays.toString((float[]) value));
      } else if (value instanceof double[]) {
        b.append(Arrays.toString((double[]) value));
      } else if (value instanceof String[]) {
        b.append(Arrays.toString((String[]) value));
      } else if (value instanceof CharSequence[]) {
        b.append(Arrays.toString((CharSequence[]) value));
      } else if (value instanceof Parcelable[]) {
        b.append(Arrays.toString((Parcelable[]) value));
      } else if (value instanceof Bundle) {
        b.append(bundleToString((Bundle) value));
      } else {
        b.append(value);
      }
      first = false;
    }
  }

  private static void intentToShortString(Intent intent, StringBuilder b) {
    boolean first = true;
    String mAction = intent.getAction();
    if (mAction != null) {
      b.append("act=").append(mAction);
      first = false;
    }
    Set<String> mCategories = intent.getCategories();
    if (mCategories != null) {
      if (!first) {
        b.append(' ');
      }
      first = false;
      b.append("cat=[");
      boolean firstCategory = true;
      for (String c : mCategories) {
        if (!firstCategory) {
          b.append(',');
        }
        b.append(c);
        firstCategory = false;
      }
      b.append("]");
    }
    Uri mData = intent.getData();
    if (mData != null) {
      if (!first) {
        b.append(' ');
      }
      first = false;
      b.append("dat=");
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        b.append(uriToSafeString(mData));
      } else {
        String scheme = mData.getScheme();
        if (scheme != null) {
          if (scheme.equalsIgnoreCase("tel")) {
            b.append("tel:xxx-xxx-xxxx");
          } else if (scheme.equalsIgnoreCase("smsto")) {
            b.append("smsto:xxx-xxx-xxxx");
          } else {
            b.append(mData);
          }
        } else {
          b.append(mData);
        }
      }
    }
    String mType = intent.getType();
    if (mType != null) {
      if (!first) {
        b.append(' ');
      }
      first = false;
      b.append("typ=").append(mType);
    }
    int mFlags = intent.getFlags();
    if (mFlags != 0) {
      if (!first) {
        b.append(' ');
      }
      first = false;
      b.append("flg=0x").append(Integer.toHexString(mFlags));
    }
    String mPackage = intent.getPackage();
    if (mPackage != null) {
      if (!first) {
        b.append(' ');
      }
      first = false;
      b.append("pkg=").append(mPackage);
    }
    ComponentName mComponent = intent.getComponent();
    if (mComponent != null) {
      if (!first) {
        b.append(' ');
      }
      first = false;
      b.append("cmp=").append(mComponent.flattenToShortString());
    }
    Rect mSourceBounds = intent.getSourceBounds();
    if (mSourceBounds != null) {
      if (!first) {
        b.append(' ');
      }
      first = false;
      b.append("bnds=").append(mSourceBounds.toShortString());
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      ClipData mClipData = intent.getClipData();
      if (mClipData != null) {
        if (!first) {
          b.append(' ');
        }
        first = false;
        b.append("(has clip)");
      }
    }
    Bundle mExtras = intent.getExtras();
    if (mExtras != null) {
      if (!first) {
        b.append(' ');
      }
      b.append("extras={");
      bundleToShortString(mExtras, b);
      b.append('}');
    }
    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
      Intent mSelector = intent.getSelector();
      if (mSelector != null) {
        b.append(" sel=");
        intentToShortString(mSelector, b);
        b.append("}");
      }
    }
  }

  private static String uriToSafeString(Uri uri) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      try {
        Method toSafeString = Uri.class.getDeclaredMethod("toSafeString");
        toSafeString.setAccessible(true);
        return (String) toSafeString.invoke(uri);
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return uri.toString();
  }
}
