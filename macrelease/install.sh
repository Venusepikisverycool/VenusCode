#!/bin/zsh

JAR_NAME="venuscode-1.0.0.jar"
INSTALL_DIR="$HOME/.venuscode"
JAR_PATH="$INSTALL_DIR/$JAR_NAME"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "Installing VenusCode..."

mkdir -p "$INSTALL_DIR"

if [ ! -f "$SCRIPT_DIR/$JAR_NAME" ]; then
    echo "Error: $JAR_NAME was not found next to install.sh."
    exit 1
fi

cp "$SCRIPT_DIR/$JAR_NAME" "$JAR_PATH"

if ! grep -q 'alias venuscode=' "$HOME/.zshrc" 2>/dev/null; then
    echo "" >> "$HOME/.zshrc"
    echo "# VenusCode" >> "$HOME/.zshrc"
    echo "alias venuscode='java -jar \"$JAR_PATH\"'" >> "$HOME/.zshrc"
fi

echo ""
echo "VenusCode installed successfully!"
echo ""
echo "Run this to activate the command:"
echo "source ~/.zshrc"
echo ""
echo "Then you can use:"
echo "venuscode encode \"Hello world\""
echo "venuscode decode <image.png>"