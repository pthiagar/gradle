/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.model.internal.manage.schema.store;

import org.gradle.model.internal.core.ModelType;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

public class InvalidManagedModelElementTypeException extends RuntimeException {

    private static String createContextPathStringRepresentation(ModelSchemaExtractionContext context, ModelType<?> type) {
        StringBuilder prefix = new StringBuilder();
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        List<String> contextPathElements = context.getContextPathElements();
        writer.println(contextPathElements.get(0));

        Iterator<String> iterator = contextPathElements.listIterator(1);
        while(iterator.hasNext()) {
            writer.print(prefix);
            writer.print("\\--- ");
            writer.println(iterator.next());
            prefix.append("     ");
        }
        writer.print(prefix);
        writer.print("\\--- ");
        writer.print(type);
        return out.toString();
    }

    private static String getMessage(ModelType<?> type, String message, ModelSchemaExtractionContext context) {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        writer.print("Invalid managed model type " + type + ": " + message);
        if (context != null) {
            writer.println(". The type was analyzed due to the following dependencies:");
            writer.print(createContextPathStringRepresentation(context, type));
        }
        return out.toString();
    }

    public InvalidManagedModelElementTypeException(ModelType<?> type, String message, ModelSchemaExtractionContext context) {
        super(getMessage(type, message, context));
    }
}
